package com.apap.tugas1.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDb;

@Service
public class PegawaiServiceImpl implements PegawaiService {
	@Autowired
	private PegawaiDb pegawaiDb;
	
	@Override
	public List<PegawaiModel> getAllDetailPegawai() {
		return pegawaiDb.findAll();
	}

	@Override
	public void addPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
	}

	@Override
	public void deletePegawai(Long id) {
		pegawaiDb.deleteById(id);
	}

	@Override
	public Optional<PegawaiModel> getPegawaiDetailById(Long id) {
		return pegawaiDb.findById(id);
	}

	@Override
	public PegawaiModel getPegawaiDetailByNip(String nip) {
		return pegawaiDb.findByNip(nip);
	}

	@Override
	public void updatePegawai(PegawaiModel newPegawai, Long id) {
		PegawaiModel pegawaiUpdated = pegawaiDb.getOne(id);
		pegawaiUpdated.setNama(newPegawai.getNama());
		pegawaiUpdated.setTahun_masuk(newPegawai.getTahun_masuk());
		pegawaiUpdated.setTempat_lahir(newPegawai.getTempat_lahir());
		pegawaiUpdated.setTanggal_lahir(newPegawai.getTanggal_lahir());
		pegawaiUpdated.setListOfJabatan(newPegawai.getListOfJabatan());
		pegawaiUpdated.setId(newPegawai.getId());
		pegawaiUpdated.setNip(newPegawai.getNip());
		pegawaiUpdated.setInstansi(newPegawai.getInstansi());
		pegawaiDb.save(pegawaiUpdated);
	}

	@Override
	public List<PegawaiModel> getPegawaiDetailByInstansi(InstansiModel instansi) {
		return pegawaiDb.findByInstansi(instansi);
	}

	@Override
	public List<PegawaiModel> getPegawaiDetailByInstansiProvinsi(List<InstansiModel> listInstansi) {
		return pegawaiDb.findByInstansiProvinsi(listInstansi);
	}


}
