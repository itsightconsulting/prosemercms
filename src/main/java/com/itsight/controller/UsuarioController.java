package com.itsight.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.constants.Utilitarios;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.SecurityRole;
import com.itsight.domain.SecurityUser;
import com.itsight.domain.Usuario;
import com.itsight.repository.SecurityRoleRepository;
import com.itsight.repository.SecurityUserRepository;
import com.itsight.service.UsuarioService;

@Controller
@RequestMapping("/gestion/usuario")
public class UsuarioController {

	private UsuarioService usuarioService;
	
	private SecurityUserRepository securityUserRepository;
	
	private SecurityRoleRepository securityRoleRepository;
	
	
	@Autowired
	public UsuarioController(UsuarioService usuarioService, SecurityUserRepository securityUserRepository, SecurityRoleRepository securityRoleRepository) {
		// TODO Auto-generated constructor stub
		this.usuarioService = usuarioService;
		this.securityUserRepository = securityUserRepository;
		this.securityRoleRepository = securityRoleRepository;
	}
	
	@GetMapping(value = "")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView principalUsuario(Model  model) {
		return new ModelAndView(ViewConstant.MAIN_USUARIO);
	}
	
	@GetMapping(value = "/obtenerListado/{comodin}/{estado}/{perfil}")
	public @ResponseBody List<Usuario> listAllUsers(
										@PathVariable("comodin") String comodin,
										@PathVariable("estado") String estado,
										@PathVariable("perfil") String perfil) throws JsonProcessingException{
		
		List<Usuario> lstUsuario = new ArrayList<Usuario>();
		
		if(comodin.equals("0") && estado.equals("-1") && perfil.equals("0") ) {
			lstUsuario = usuarioService.listAll();	
		}else {
			if(comodin.equals("0") && perfil.equals("0")) {
				lstUsuario = usuarioService.findAllByFlagActivo(Boolean.valueOf(estado));
			}else {
				List<Usuario> lstUsuarioFilter = new ArrayList<>();
				comodin = comodin.equals("0") ? "":comodin;
				
				lstUsuario = usuarioService.findAllByApellidoPaternoContainingOrApellidoMaternoContainingOrNombres(comodin, comodin, comodin);
				if(!estado.equals("-1")) {
					for (Usuario x : lstUsuario) {
							
						if(!perfil.equals("0") && Boolean.valueOf(estado).equals(x.isFlagActivo())) {
							if(perfil.equals(String.valueOf(x.getPerfil().getId()))){
								lstUsuarioFilter.add(x);
							}
						}else if(perfil.equals("0") && Boolean.valueOf(estado).equals(x.isFlagActivo())){
								lstUsuarioFilter.add(x);
						}
					}
					
					return lstUsuarioFilter;
				}else {
					if(!perfil.equals("0")) {
						for (Usuario x : lstUsuario) {
							if(perfil.equals(String.valueOf(x.getPerfil().getId()))){
								lstUsuarioFilter.add(x);
							}
						}
						return lstUsuarioFilter;
					}
				}
			}
		}
		
		return lstUsuario;
	}
	
	@GetMapping(value = "/obtenerUsuario")
	public @ResponseBody Usuario getUserById(@RequestParam(value = "id") int usuarioId) {
		return usuarioService.getUsuarioById(usuarioId);
	}
	
	@PostMapping(value = "/agregar")
	public @ResponseBody String addUser(@ModelAttribute Usuario usuario) {
			String role = "";
			int rol = usuario.getFkPerfil();
			if(rol == 1) {
				role = "ROLE_ADMIN";
			}else {
				role = "ROLE_EDITOR";
			}
			
			usuario.setPerfil(usuario.getFkPerfil());
			if(usuario.getId() == 0 ){//Registro
				if(securityUserRepository.findByUsername(usuario.getUsername()) == null) {
					usuario.setPassword(Utilitarios.encoderPassword(usuario.getPassword()));
					usuario.setFechaCreacion();
					usuarioService.add(usuario);
					
					//Añadiendo las credenciales de ingreso
					SecurityUser secUser = new SecurityUser();
					secUser.setUsername(usuario.getUsername());
					secUser.setPassword(usuario.getPassword());
					secUser.setEnabled(usuario.isFlagActivo());
					//Añadiendo el role de colaborador
					SecurityRole secUserRole = new SecurityRole();
					secUserRole.setRole(role);
					
					secUserRole.setSecurityUser(secUser);
					Set<SecurityRole> listSr = new HashSet<>();
					listSr.add(secUserRole);
					//Adding to User
					secUser.setRoles(listSr);
					securityUserRepository.save(secUser);
					return "1";
				}
				return "-9";
			}else {//Actualización
				
				String qUsuarioPassword = usuarioService.getPasswordById(usuario.getId());
				//Comprobando password
				if(usuario.getPassword().equals(qUsuarioPassword.substring(0, 10))){
					usuario.setPassword(qUsuarioPassword);
				}else{
					String newPassword = Utilitarios.encoderPassword(usuario.getPassword());
					//TB_Usuario
					usuario.setPassword(newPassword);
					//TB_SecurityUser |Username = Primary Key, en el contexto
					securityUserRepository.save(new SecurityUser(usuario.getUsername(), newPassword, usuario.isFlagActivo()));
				}
				
				securityUserRepository.saveUserStatusByUsername(usuario.isFlagActivo(), usuario.getUsername());
				
				//Comprando Role/Perfil
				
				SecurityRole qSecurityRole = securityRoleRepository.findBySecurityUserUsername(usuario.getUsername());
				if(!role.equals(qSecurityRole.getRole())){
					//Actualizando el rol
					qSecurityRole.setRole(role);
					securityRoleRepository.save(qSecurityRole);
				}
				
				usuario.setFechaModificacion();
				usuarioService.update(usuario);
				return "2";
			}
	}
	
	@PostMapping(value = "/desactivarUsuario")
	public @ResponseBody String disabledUser(@RequestParam(value = "id") int usuarioId) {
			Usuario usuario = usuarioService.getUsuarioById(usuarioId);

			if(usuario.isFlagActivo()) {
				usuario.setFlagActivo(false);
			}else {
				usuario.setFlagActivo(true);
			}
			
			usuario.setFechaModificacion();
			usuarioService.update(usuario);
			
			//TB_SecurityUser |Username = Primary Key, en el contexto
			securityUserRepository.save(new SecurityUser(usuario.getUsername(), usuario.getPassword(), usuario.isFlagActivo()));
			
			return "1";
	}
}
