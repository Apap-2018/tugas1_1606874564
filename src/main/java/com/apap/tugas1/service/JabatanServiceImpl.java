package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDb;

@Service
public class JabatanServiceImpl implements JabatanService{
	@Autowired
	private JabatanDb jabatanDb;

	@Override
	public void addJabatan(JabatanModel jabatan) {
		jabatanDb.save(jabatan);
		
	}

	@Override
	public List<JabatanModel> getAllDetailJabatan() {
		return jabatanDb.findAll();
	}

	@Override
	public JabatanModel getJabatanById(long id) {
		return jabatanDb.findById(id);
	}

	@Override
	public void updateJabatan(JabatanModel newJabatan, Long id) {
		JabatanModel jabatanUpdated = jabatanDb.getOne(id);
		jabatanUpdated.setNama(newJabatan.getNama());
		jabatanUpdated.setDeskripsi(newJabatan.getDeskripsi());
		jabatanUpdated.setGaji_pokok(newJabatan.getGaji_pokok());
		jabatanDb.save(jabatanUpdated);
		
	}

	@Override
	public void deleteJabatan(Long id) {
		jabatanDb.deleteById(id);		
	}
	
}
