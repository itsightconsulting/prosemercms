package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.free.Imagen;
import com.itsight.repository.ImagenRepository;
import com.itsight.service.ImagenService;

@Service
@Transactional
public class ImagenServiceImpl implements ImagenService {

	
	private ImagenRepository imagenRepository;
	
	@Autowired
	public ImagenServiceImpl(ImagenRepository imagenRepository) {
		// TODO Auto-generated constructor stub
		this.imagenRepository = imagenRepository;
	}
	
	@Override
	public List<Imagen> listAll() {
		// TODO Auto-generated method stub
		return imagenRepository.findAll();
	}

	@Override
	public List<Imagen> findAllById(int id) {
		// TODO Auto-generated method stub
		return imagenRepository.findAllById(id);
	}

	@Override
	public List<Imagen> findById(int id) {
		// TODO Auto-generated method stub
		return imagenRepository.findImagenById(id);
	}

	@Override
	public Imagen getImagenById(int id) {
		// TODO Auto-generated method stub
		return imagenRepository.findOne(new Integer(id));
	}

	@Override
	public Imagen add(Imagen imagen) {
		// TODO Auto-generated method stub
		return imagenRepository.save(imagen);
	}

	@Override
	public Imagen update(Imagen imagen) {
		// TODO Auto-generated method stub
		return imagenRepository.saveAndFlush(imagen);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		imagenRepository.delete(new Integer(id));
	}

}
