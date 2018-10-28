package com.apap.tugas1.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "provinsi")
public class ProvinsiModel implements Serializable {
	@Id
	@NotNull
	@Size(max = 10)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Size(max = 225)
	@Column(name = "nama", nullable = false)
	private String nama;
	
	@NotNull
	@Column(name = "presentase_tunjangan", nullable = false)
	private double presentase_tunjangan;
	
	@OneToMany(mappedBy = "provinsi", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<InstansiModel> listInstansi;

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public double getPresentase_tunjangan() {
		return presentase_tunjangan;
	}

	public void setPresentase_tunjangan(double presentase_tunjangan) {
		this.presentase_tunjangan = presentase_tunjangan;
	}

	public List<InstansiModel> getListInstansi() {
		return listInstansi;
	}

	public void setListInstansi(List<InstansiModel> listInstansi) {
		this.listInstansi = listInstansi;
	}
	
	

}
