package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.repository.InstansiDb;

@Service
public class InstansiServiceImpl implements InstansiService{
	@Autowired
	InstansiDb instansiDb;

	@Override
	public Optional<InstansiModel> getInstansiDetailById(Long id) {
		return instansiDb.findById(id);
		
	}

	@Override
	public List<InstansiModel> getAllInstansiDetail() {
		return instansiDb.findAll();
	}
	

}
