package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Beneficiario;
import com.itsight.repository.BeneficiarioRepository;
import com.itsight.service.BeneficiarioService;

@Service
@Transactional
public class BeneficiarioServiceImpl implements BeneficiarioService {
	
	private BeneficiarioRepository beneficiarioRepository;
	
	@Autowired
	public BeneficiarioServiceImpl(BeneficiarioRepository beneficiarioRepository) {
		this.beneficiarioRepository = beneficiarioRepository;
	}

	@Override
	public List<Beneficiario> listAll() {
		// TODO Auto-generated method stub
		return beneficiarioRepository.findAll();
	}

	@Override
	public Beneficiario add(Beneficiario beneficiario) {
		// TODO Auto-generated method stub
		return beneficiarioRepository.save(beneficiario);
	}

	@Override
	public Beneficiario update(Beneficiario beneficiario) {
		// TODO Auto-generated method stub
		return beneficiarioRepository.saveAndFlush(beneficiario);
	}

	@Override
	public void delete(int beneficiarioId) {
		// TODO Auto-generated method stub
		beneficiarioRepository.delete(new Integer(beneficiarioId));
	}

	@Override
	public Beneficiario findOneById(int beneficiarioId) {
		// TODO Auto-generated method stub
		return beneficiarioRepository.findOne(new Integer(beneficiarioId));
	}

}
