package com.apap.tugas1.controller;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;


@Controller
public class PegawaiController {
	@Autowired
	PegawaiService pegawaiService;
	
	@Autowired
	JabatanService jabatanService;
	
	@Autowired
	InstansiService instansiService;
	
	@Autowired
	ProvinsiService provinsiService;
	
	@RequestMapping("/")
	private String home(Model model) {
		List<JabatanModel> jabatan = jabatanService.getAllDetailJabatan();
		List<InstansiModel> instansi = instansiService.getAllInstansiDetail();
		model.addAttribute("instansi", instansi);
		model.addAttribute("jabatan", jabatan);
		return "home";	
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		List<JabatanModel> listJabatan = jabatanService.getAllDetailJabatan();
		List<InstansiModel> listInstansi = instansiService.getAllInstansiDetail();
		List<ProvinsiModel> listProvinsi = provinsiService.getAllDetailProvinsi();
		model.addAttribute("listPegawai", new PegawaiModel());
		model.addAttribute("jabatan", listJabatan);
		model.addAttribute("listInstansi", listInstansi);
		model.addAttribute("listProvinsi", listProvinsi);
		return "add-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params={"addRow"})
	private String addRow(@ModelAttribute PegawaiModel pegawai, Model model) {
		if(pegawai.getListOfJabatan() == null) {
			pegawai.setListOfJabatan(new HashSet<>());
		}
		pegawai.getListOfJabatan().add(new JabatanModel());
		List<JabatanModel> jabatans = jabatanService.getAllDetailJabatan();
		List<ProvinsiModel> provinsi = provinsiService.getAllDetailProvinsi();
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listJabatan", jabatans);
		model.addAttribute("listProvinsi", provinsi);
		return "add-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah/instansi", method = RequestMethod.GET)
	private @ResponseBody List<InstansiModel> cekInstansi(@RequestParam(value="provinsiId") long provinsiId) {
		ProvinsiModel getProv = provinsiService.getProvinsiDetailById(provinsiId).get();
		return getProv.getListInstansi();
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params= {"submit"})
	private String addRowSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		String nip = generateNip(pegawai);
		pegawai.setNip(nip);
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("nip", nip);
		return "add";
	}
	
	
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	private String viewPegawai(@RequestParam("nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		Set<JabatanModel> listJabatan = pegawai.getListOfJabatan();
		double count = 0;
		ProvinsiModel provinsi = pegawai.getInstansi().getProvinsi();
		for(JabatanModel jabatan:listJabatan) {
			if(jabatan.getGaji_pokok() > count) {
				count = jabatan.getGaji_pokok();
			}
		}
		double tunjangan = provinsi.getPresentase_tunjangan();
		int gaji = (int)(count + ((tunjangan / 100) * (int)count));
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listJabatan", listJabatan);
		model.addAttribute("gaji", gaji);
		return "view-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/termuda-tertua", method = RequestMethod.GET)
	private String tertuaTermuda(@RequestParam("idInstansi") Long idInstansi, Model model) {
		List<PegawaiModel> list = pegawaiService.getAllDetailPegawai();
		Optional<InstansiModel> instansi = instansiService.getInstansiDetailById(idInstansi);
		ArrayList<PegawaiModel> pegawaiDummy = new ArrayList<>();
		for(PegawaiModel peg:list) {
			if(peg.getInstansi().equals(instansi.get())) {
				pegawaiDummy.add(peg);
			}
		}
		Collections.sort(pegawaiDummy, new AgeComparator());
		PegawaiModel tertua = pegawaiDummy.get(0);
		PegawaiModel termuda = pegawaiDummy.get(pegawaiDummy.size() - 1);
		model.addAttribute("instansi", instansi);
		model.addAttribute("termuda", termuda);
		model.addAttribute("tertua", tertua);
		return "tertua-termuda";
	}
	
	private String generateNip(PegawaiModel pegawai) {
		Date tanggalLahir = pegawai.getTanggal_lahir();
		DateFormat dateFormat = new SimpleDateFormat("ddMMYY");
		String strDate = dateFormat.format(tanggalLahir);
		
		Long kodeInstansi = pegawai.getInstansi().getId();
		String tahunMasuk = pegawai.getTahun_masuk();
		
		int nomorAkhir = 0;
		for(PegawaiModel peg:pegawaiService.getAllDetailPegawai()) {
			if((peg.getTanggal_lahir().equals(pegawai.getTanggal_lahir())) && (peg.getTahun_masuk().equals(pegawai.getTahun_masuk()))){
				nomorAkhir += 1;
			}
		}
		nomorAkhir += 1;
		
		String finalAkhir = "";
		if(nomorAkhir < 10) {
			finalAkhir = "0" + Integer.toString(nomorAkhir);
		}
		else {
			finalAkhir = Integer.toString(nomorAkhir);
		}
		
		String nipAkhir = kodeInstansi + strDate + tahunMasuk + finalAkhir;
		System.out.println(nipAkhir);
		return nipAkhir;
	}
	
	public static class AgeComparator implements Comparator<PegawaiModel> {
		
	    public int compare(PegawaiModel p1, PegawaiModel p2) {
	        if(p1.getTanggal_lahir().compareTo(p2.getTanggal_lahir()) < 0) {
	        	return -1;
	        }
	        else if(p1.getTanggal_lahir().compareTo(p2.getTanggal_lahir()) > 0) {
	        	return 1;
	        }
	        else {
	        	return 0;
	        }
	    }

	    public boolean equals(PegawaiModel p1, PegawaiModel p2) {
	        return this.compare(p1, p2) == 0;
	    }
	}

}
