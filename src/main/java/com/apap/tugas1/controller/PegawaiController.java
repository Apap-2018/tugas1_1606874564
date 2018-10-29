package com.apap.tugas1.controller;
import java.math.BigInteger;
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

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	
	@RequestMapping(value = "/pegawai/cari", method = RequestMethod.GET)
	private String find(@RequestParam(value = "idProvinsi", required = false) Optional<String> idProvinsi, 
			@RequestParam(value = "idInstansi", required = false) Optional<String> id_instansi,
			@RequestParam(value = "idJabatan", required = false) Optional<String> id_jabatan, Model model) {
		List<ProvinsiModel> provinsiDetail = provinsiService.getAllDetailProvinsi();
		List<InstansiModel> instansiDetail = instansiService.getAllInstansiDetail();
		List<JabatanModel> jabatanDetail = jabatanService.getAllDetailJabatan();
		model.addAttribute("allProvinsi", provinsiDetail);
		model.addAttribute("allInstansi", instansiDetail);
		model.addAttribute("allJabatan", jabatanDetail);
		
		// ada provinsi dan instansi
		if(idProvinsi.isPresent() && id_instansi.isPresent()) {
			InstansiModel instansi = instansiService.getInstansiDetailById(Long.parseLong(id_instansi.get())).get();
			List<PegawaiModel> allPegawaiInProvinsiInstansi = pegawaiService.getPegawaiDetailByInstansi(instansi);
			model.addAttribute("allPegawai", allPegawaiInProvinsiInstansi);
		}
		
		// ada provinsi dan jabatan
		else if(idProvinsi.isPresent() && id_jabatan.isPresent()) {
			ProvinsiModel provinsi = provinsiService.getProvinsiDetailById(Long.parseLong(idProvinsi.get())).get();
			List<InstansiModel> allInstansiByProvinsi = instansiService.getInstansiByProvinsi(provinsi);
			
			JabatanModel jabatan = jabatanService.getJabatanById(Long.parseLong(id_jabatan.get()));
			
			List<PegawaiModel> allPegawaiInProvinsiJabatan = new ArrayList<>();
			List<PegawaiModel> allPegawai = pegawaiService.getAllDetailPegawai();
			
			// iterasiin pegawai
			for(int i = 0; i < allPegawai.size(); i++) {
				if(allInstansiByProvinsi.contains(allPegawai.get(i).getInstansi())) {
					// iterasiin jabatan di pegawai
					for(int j = 0; j < allPegawai.get(i).getListOfJabatan().size(); j++) {
						// kalo jabatannya sama
						if(allPegawai.get(j).getListOfJabatan().get(j) == jabatan) {
							allPegawaiInProvinsiJabatan.add(allPegawai.get(i));
						}
					}
				}
			}
			model.addAttribute("allPegawai", allPegawaiInProvinsiJabatan);
		}
		
		// ada instansi dan jabatan
		else if(id_instansi.isPresent() && id_jabatan.isPresent()) {
		
			InstansiModel instansi = instansiService.getInstansiDetailById(Long.parseLong(id_instansi.get())).get();
			List<PegawaiModel> allPegawaiByInstansi = pegawaiService.getPegawaiDetailByInstansi(instansi);
			
			JabatanModel jabatan = jabatanService.getJabatanById(Long.parseLong(id_jabatan.get()));
			
			List<PegawaiModel> allPegawaiInstansiJabatan = new ArrayList<>();
			List<PegawaiModel> allPegawai = pegawaiService.getAllDetailPegawai();
			
			for(int i = 0; i < allPegawai.size(); i++) {
				if(allPegawaiByInstansi.contains(allPegawai.get(i))) {
					for(int j = 0; j < allPegawai.get(i).getListOfJabatan().size(); j++) {
						if(allPegawai.get(j).getListOfJabatan().get(j) == jabatan) {
							allPegawaiInstansiJabatan.add(allPegawai.get(i));
						}
					}
				}
			}
			model.addAttribute("allPegawai", allPegawaiInstansiJabatan);	
		}
		
		// ada provinsi
		else if(idProvinsi.isPresent()) {
			ProvinsiModel provinsi = provinsiService.getProvinsiDetailById(Long.parseLong(idProvinsi.get())).get();
			List<InstansiModel> allInstansiByProvinsi = instansiService.getInstansiByProvinsi(provinsi);
			List<PegawaiModel> allPegawaiByInstansiProvinsi = pegawaiService.getPegawaiDetailByInstansiProvinsi(allInstansiByProvinsi);
			
			List<PegawaiModel> allPegawai = pegawaiService.getAllDetailPegawai();
			List<PegawaiModel> allPegawaiInProvinsi = new ArrayList<>();
			
			for(int i = 0; i < allPegawai.size(); i++) {
				if(allPegawaiByInstansiProvinsi.get(i).equals(allPegawai.get(i))) {
					allPegawaiInProvinsi.add(allPegawai.get(i));
				}
			}
			model.addAttribute("allPegawai", allPegawaiInProvinsi);
		}
		
		// ada instansi
		else if(id_instansi.isPresent()) {
			InstansiModel instansi = instansiService.getInstansiDetailById(Long.parseLong(id_instansi.get())).get();
			List<PegawaiModel> allPegawaiByInstansi = pegawaiService.getPegawaiDetailByInstansi(instansi);
			
			List<PegawaiModel> allPegawai = pegawaiService.getAllDetailPegawai();
			List<PegawaiModel> allPegawaiInInstansi = new ArrayList<>();
			
			for(int i = 0; i < allPegawai.size(); i++) {
				if(allPegawaiByInstansi.get(i).equals(allPegawai.get(i))) {
					allPegawaiInInstansi.add(allPegawai.get(i));
				}
			}
			model.addAttribute("allPegawai", allPegawaiInInstansi);	
		}
		
		// ada jabatan
		else if(id_jabatan.isPresent()) {
			JabatanModel jabatan = jabatanService.getJabatanById(Long.parseLong(id_jabatan.get()));
			
			List<PegawaiModel> allPegawai = pegawaiService.getAllDetailPegawai();
			List<PegawaiModel> allPegawaiInJabatan = new ArrayList<>();
			
			for(int i =0; i < allPegawai.size(); i++) {
				for(int j = 0; j < allPegawai.get(i).getListOfJabatan().size(); j++) {
					if(allPegawai.get(i).getListOfJabatan().get(j) == jabatan) {
						allPegawaiInJabatan.add(allPegawai.get(i));
					}
				}
			}
			model.addAttribute("allPegawai", allPegawaiInJabatan);
			
		}
		
		// ada provinsi, instansi, jabatan
		else if(idProvinsi.isPresent() && id_instansi.isPresent() && id_jabatan.isPresent()){
			InstansiModel instansi = instansiService.getInstansiDetailById(Long.parseLong(id_instansi.get())).get();
			List<PegawaiModel> allPegawai = pegawaiService.getPegawaiDetailByInstansi(instansi);
			
			JabatanModel jabatan = jabatanService.getJabatanById(Long.parseLong(id_jabatan.get()));
			List<PegawaiModel> pegawaiInJabatanInstansiProvinsi = new ArrayList<>();
			for(int i = 0; i < allPegawai.size(); i++) {
				for(int j = 0; j < allPegawai.get(i).getListOfJabatan().size(); j++) {
					if(allPegawai.get(j).getListOfJabatan().get(j)==jabatan) {
						pegawaiInJabatanInstansiProvinsi.add(allPegawai.get(i));
					}
				}
			}
			model.addAttribute("allPegawai", pegawaiInJabatanInstansiProvinsi);
		}
		return "cari-pegawai";
	}
	
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		PegawaiModel pegawai = new PegawaiModel();
		if(pegawai.getListOfJabatan() == null) {
			pegawai.setListOfJabatan(new ArrayList<JabatanModel>());
		}
		
		pegawai.getListOfJabatan().add(new JabatanModel());
		List<JabatanModel> listJabatan = jabatanService.getAllDetailJabatan();
		List<ProvinsiModel> listProvinsi = provinsiService.getAllDetailProvinsi();
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listOfJabatan", listJabatan);
		model.addAttribute("listProvinsi", listProvinsi);
		return "add-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params={"addRow"})
	private String addRow(@ModelAttribute PegawaiModel pegawai, Model model, BindingResult bindingResult) {
		if(pegawai.getListOfJabatan() == null) {
			pegawai.setListOfJabatan(new ArrayList<>());
		}
		
		pegawai.getListOfJabatan().add(new JabatanModel());
		List<JabatanModel> jabatan = jabatanService.getAllDetailJabatan();
		List<ProvinsiModel> provinsi = provinsiService.getAllDetailProvinsi();
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listOfJabatan", jabatan);
		model.addAttribute("listProvinsi", provinsi);
		return "add-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params={"deleteRow"})
	private String removeRow(@ModelAttribute PegawaiModel pegawai, Model model, HttpServletRequest req, BindingResult bindingResult) {
		int index = Integer.parseInt(req.getParameter("deleteRow"));
		pegawai.getListOfJabatan().remove(index);
		
		List<JabatanModel> jabatan = jabatanService.getAllDetailJabatan();
		List<ProvinsiModel> provinsi = provinsiService.getAllDetailProvinsi();
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listOfJabatan", jabatan);
		model.addAttribute("listProvinsi", provinsi);
		return "add-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah/instansi", method = RequestMethod.GET)
	private @ResponseBody List<InstansiModel> cekInstansi(@RequestParam(value="provinsiId") long provinsiId) {
		ProvinsiModel getProv = provinsiService.getProvinsiDetailById(provinsiId).get();
		return getProv.getListInstansi();
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params= {"save"})
	private String addRowSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		String nip = generateNip(pegawai);
		//System.out.println(nip);
		pegawai.setNip(nip);
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("nip", nip);
		return "add";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.GET)
	private String ubahPegawai(@RequestParam String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		List<JabatanModel> listJabatan = jabatanService.getAllDetailJabatan();
		List<ProvinsiModel> listProvinsi = provinsiService.getAllDetailProvinsi();
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listOfJabatan", listJabatan);
		model.addAttribute("listProvinsi", listProvinsi);
		return "ubah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST, params= {"addRow"})
	private String addRowUbahPegawai(@ModelAttribute PegawaiModel pegawai, Model model, BindingResult bindingResult) {
		if(pegawai.getListOfJabatan() == null) {
			pegawai.setListOfJabatan(new ArrayList<>());
		}
		
		pegawai.getListOfJabatan().add(new JabatanModel());
		List<JabatanModel> jabatan = jabatanService.getAllDetailJabatan();
		List<ProvinsiModel> provinsi = provinsiService.getAllDetailProvinsi();
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listOfJabatan", jabatan);
		model.addAttribute("listProvinsi", provinsi);
		return "ubah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST, params={"deleteRow"})
	private String removeRowUbahPegawai(@ModelAttribute PegawaiModel pegawai, Model model, HttpServletRequest req, BindingResult bindingResult) {
		int index = Integer.parseInt(req.getParameter("deleteRow"));
		pegawai.getListOfJabatan().remove(index);
		
		List<JabatanModel> jabatan = jabatanService.getAllDetailJabatan();
		List<ProvinsiModel> provinsi = provinsiService.getAllDetailProvinsi();
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listOfJabatan", jabatan);
		model.addAttribute("listProvinsi", provinsi);
		return "ubah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST, params= {"save"})
	private String ubahPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		String nipBaru = generateNip(pegawai);
		pegawai.setNip(nipBaru);
		pegawaiService.updatePegawai(pegawai, pegawai.getId());
		model.addAttribute("nipBaru", nipBaru);
		return "ubah-pegawai-sukses";
	}
	
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	private String viewPegawai(@RequestParam("nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		List<JabatanModel> listJabatan = pegawai.getListOfJabatan();
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
		model.addAttribute("nip", nip);
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
