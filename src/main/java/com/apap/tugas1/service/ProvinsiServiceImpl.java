package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.repository.ProvinsiDb;

@Service
public class ProvinsiServiceImpl implements ProvinsiService{
	@Autowired
	ProvinsiDb provinsiDb;

	@Override
	public List<ProvinsiModel> getAllDetailProvinsi() {
		return provinsiDb.findAll();
	}

	@Override
	public Optional<ProvinsiModel> getProvinsiDetailById(Long id) {
		return provinsiDb.findById(id);
	}


}
