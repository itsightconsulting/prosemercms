package com.itsight.service;

import java.util.List;

import com.itsight.domain.Usuario;

public interface UsuarioService {

	List<Usuario> listAll();
	
	Usuario add(Usuario usuario);
	
	Usuario update(Usuario usuario);
	
	void delete(int usuarioId);
	
	Usuario getUsuarioById(int usuarioId);
	
	Usuario getUsuarioByUsername(String username);
	
	List<Usuario> findAllByFlagActivo(Boolean flagActivo);
	
	List<Usuario> findAllByApellidoPaternoContainingOrApellidoMaternoContainingOrNombres(String apellidoPaterno, String apellidoMaterno, String nombres);

	String getPasswordById(int usuarioId);

	Usuario findOneById(int id);
	
	Integer findIdByUsername(String username);
	
}
