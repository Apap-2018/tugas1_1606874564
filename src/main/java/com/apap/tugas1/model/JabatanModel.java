package com.apap.tugas1.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.*;
import javax.persistence.*;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jabatan")
public class JabatanModel implements Serializable{
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "nama", nullable = false)
	private String nama;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "deskripsi", nullable = false)
	private String deskripsi;
	
	@NotNull
	@Column(name = "gaji_pokok", nullable = false)
	private double gaji_pokok;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "listOfJabatan")
	private Set<PegawaiModel> pegawaiJabatan = new HashSet();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public double getGaji_pokok() {
		return gaji_pokok;
	}

	public void setGaji_pokok(double gaji_pokok) {
		this.gaji_pokok = gaji_pokok;
	}

	public Set<PegawaiModel> getPegawaiJabatan() {
		return pegawaiJabatan;
	}

	public void setPegawaiJabatan(Set<PegawaiModel> pegawaiJabatan) {
		this.pegawaiJabatan = pegawaiJabatan;
	}

}
