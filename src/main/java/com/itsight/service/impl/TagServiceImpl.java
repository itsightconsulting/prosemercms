package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Tag;
import com.itsight.repository.TagRepository;
import com.itsight.service.TagService;

@Service
@Transactional
public class TagServiceImpl implements TagService {
	
	private TagRepository tagRepository;
	
	@Autowired
	public TagServiceImpl(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	@Override
	public List<Tag> findAll() {
		// TODO Auto-generated method stub
		return tagRepository.findAllByOrderByNombreAsc();
	}
	
	@Override
	public List<Tag> listAll() {
		// TODO Auto-generated method stub
		return tagRepository.listAll();
	}

	@Override
	public List<Tag> findAllByTipoTagId(int tipo) {
		// TODO Auto-generated method stub
		return tagRepository.findAllByTipoTagIdOrderByNombreAsc(tipo);
	}

	@Override
	public List<Tag> listAllByTipoTag(int tipo) {
		// TODO Auto-generated method stub
		return tagRepository.listAllByTipoTag(tipo);
	}

	@Override
	public List<Tag> findAllByNombreContainingIgnoreCase(String comodin) {
		// TODO Auto-generated method stub
		return tagRepository.findAllByNombreContainingIgnoreCaseOrderByNombreAsc(comodin);
	}

	@Override
	public Tag add(Tag tag) {
		// TODO Auto-generated method stub
		return tagRepository.save(tag);
	}

	@Override
	public Tag update(Tag tag) {
		// TODO Auto-generated method stub
		return tagRepository.saveAndFlush(tag);
	}

	@Override
	public void delete(int tagId) {
		// TODO Auto-generated method stub
		tagRepository.delete(new Integer(tagId));
	}

	@Override
	public Tag findOneById(int tagId) {
		// TODO Auto-generated method stub
		return tagRepository.findOne(new Integer(tagId));
	}

	@Override
	public Tag findOneByNombre(String nombre) {
		// TODO Auto-generated method stub
		return tagRepository.findOneByNombreIgnoreCase(nombre);
	}
}
