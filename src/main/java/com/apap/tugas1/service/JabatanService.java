package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	void addJabatan(JabatanModel jabatan);
	void updateJabatan(JabatanModel jabatan, Long id);
	void deleteJabatan(Long id);
	List<JabatanModel> getAllDetailJabatan();
	JabatanModel getJabatanById(long id);
}
