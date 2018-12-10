package com.itsight.service;

import java.util.List;

import com.itsight.domain.Tag;

public interface TagService {

	
	List<Tag> findAll();
	
	List<Tag> listAll();
	
	List<Tag> listAllByTipoTag(int tipo);
	
	List<Tag> findAllByTipoTagId(int tipo);
	
	List<Tag> findAllByNombreContainingIgnoreCase(String comodin);
	
	Tag add(Tag tag);
	
	Tag update(Tag tag);
	
	void delete(int tagId);
	
	Tag findOneById(int tagId);
	
	Tag findOneByNombre(String nombre);
	
}
