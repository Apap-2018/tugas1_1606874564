package com.apap.tugas1.controller;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;

@Controller
public class JabatanController {
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private PegawaiService pegawaiService;
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("jabatan", new JabatanModel());
		return "add-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	private String addJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.addJabatan(jabatan);
		model.addAttribute("jabatan", jabatan);
		return "add-jabatan-sukses";
	}
	
	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	private String viewJabatan(@RequestParam("idJabatan") Long idJabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanById(idJabatan);
		model.addAttribute("jabatan",jabatan);
		return "view-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.GET)
	private String ubahJabatan(@RequestParam Long idJabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanById(idJabatan);
		model.addAttribute("jabatan", jabatan);
		return "ubah-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	private String ubahJabatanSubmit(@ModelAttribute JabatanModel jabatan) {
		jabatanService.updateJabatan(jabatan, jabatan.getId());
		return "ubah-jabatan-submit";
	}
	
	@RequestMapping(value = "/jabatan/hapus", method = RequestMethod.GET)
	private String deleteJabatan(@RequestParam Long idJabatan, Model model) {
		String nama = jabatanService.getJabatanById(idJabatan).getNama();
		if(jabatanService.getJabatanById(idJabatan).getPegawaiJabatan().isEmpty()) {
			jabatanService.deleteJabatan(idJabatan);
			model.addAttribute("nama", nama);
			return "delete-jabatan";
		}
		else {
			return "error/error-delete-jabatan";
		}
	}
	
	@RequestMapping(value = "/jabatan/viewall", method = RequestMethod.GET)
	private String viewAll(Model model) {
		List<JabatanModel> listAll = jabatanService.getAllDetailJabatan();
		model.addAttribute("jabatan", listAll);
		return "view-all";
	}
	
	@RequestMapping(value = "/jabatan/viewJumlahPegawai", method = RequestMethod.GET)
	private String viewJumlahPegawai(Model model) {
		Map<JabatanModel, Integer> jumlahPegawai = new HashMap<>();
		for(JabatanModel jabatan : jabatanService.getAllDetailJabatan()) {
			jumlahPegawai.put(jabatan, jabatan.getPegawaiJabatan().size());
		}
		model.addAttribute("jumlahPegawai", jumlahPegawai);
		return "jumlah-pegawai";
	}

}


