package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	Optional<PegawaiModel> getPegawaiDetailById(Long id);
	PegawaiModel getPegawaiDetailByNip(String nip);
	List<PegawaiModel> getAllDetailPegawai();
	void addPegawai(PegawaiModel pegawai);
	void deletePegawai(Long id);
}
