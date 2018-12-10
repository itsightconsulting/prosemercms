package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Usuario;
import com.itsight.repository.UsuarioRepository;
import com.itsight.service.UsuarioService;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

	
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<Usuario> listAll() {
		// TODO Auto-generated method stub
		return usuarioRepository.findAll();
	}

	@Override
	public List<Usuario> findAllByFlagActivo(Boolean flagActivo) {
		// TODO Auto-generated method stub
		return usuarioRepository.findAllByFlagActivo(flagActivo);
	}

	@Override
	public Usuario getUsuarioById(int usuarioId) {
		// TODO Auto-generated method stub
		return usuarioRepository.findOne(new Integer(usuarioId));
	}

	@Override
	public Usuario add(Usuario usuario) {
		// TODO Auto-generated method stub
		return usuarioRepository.save(usuario);
	}

	@Override
	public Usuario update(Usuario usuario) {
		// TODO Auto-generated method stub
		return usuarioRepository.saveAndFlush(usuario);
	}

	@Override
	public void delete(int usuarioId) {
		// TODO Auto-generated method stub
		usuarioRepository.delete(new Integer(usuarioId));
	}

	@Override
	public List<Usuario> findAllByApellidoPaternoContainingOrApellidoMaternoContainingOrNombres(
			String apellidoPaterno, String apellidoMaterno, String nombres) {
		// TODO Auto-generated method stub
		return usuarioRepository.findAllByApellidoPaternoContainingIgnoreCaseOrApellidoMaternoContainingIgnoreCaseOrNombresContainingIgnoreCase(apellidoPaterno, apellidoMaterno, nombres);
	}

	@Override
	public String getPasswordById(int usuarioId) {
		// TODO Auto-generated method stub
		return usuarioRepository.findPasswordById(usuarioId);
	}

	@Override
	public Usuario findOneById(int id) {
		// TODO Auto-generated method stub
		return usuarioRepository.findOne(id);
	}

	@Override
	public Usuario getUsuarioByUsername(String username) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByUsername(username);
	}

	@Override
	public Integer findIdByUsername(String username) {
		// TODO Auto-generated method stub
		return usuarioRepository.findIdByUsername(username);
	}

}
