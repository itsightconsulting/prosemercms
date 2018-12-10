package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

	@Override
    @EntityGraph(value="usuario.perfil", attributePaths = {})
    List<Usuario> findAll();
	
	@EntityGraph(value="usuario.perfil", attributePaths = {})
	List<Usuario> findAllByFlagActivo(Boolean flagActivo);
	
	@EntityGraph(value="usuario.perfil", attributePaths = {})
	List<Usuario> findAllByApellidoPaternoContainingIgnoreCaseOrApellidoMaternoContainingIgnoreCaseOrNombresContainingIgnoreCase(String apellidoPaterno, String apellidoMaterno, String nombres);
	
	@Query("SELECT U.password FROM Usuario U WHERE U.id = ?1")
	String findPasswordById(int usuarioId);

	@EntityGraph(value="usuario", attributePaths = {})
	Usuario findByUsername(String username);
	
	@Query("SELECT U.id FROM Usuario U WHERE U.username = ?1")
	Integer findIdByUsername(String username);
	
}
