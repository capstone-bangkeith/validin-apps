package com.safiraak.validin.data.remote

import com.google.gson.annotations.SerializedName

data class RecognitionResponse(

	@field:SerializedName("data")
	val data: Data? = null
)

data class Ktp(

	@field:SerializedName("provinsi")
	val provinsi: String? = null,

	@field:SerializedName("kota")
	val kota: String? = null,

	@field:SerializedName("agama")
	val agama: String? = null,

	@field:SerializedName("ttl")
	val ttl: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("kewarganegaraan")
	val kewarganegaraan: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("kel_desa")
	val kelDesa: String? = null,

	@field:SerializedName("pekerjaan")
	val pekerjaan: String? = null,

	@field:SerializedName("kecamatan")
	val kecamatan: String? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null,

	@field:SerializedName("rt_rw")
	val rtRw: String? = null,

	@field:SerializedName("status_perkawinan")
	val statusPerkawinan: String? = null,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String? = null
)

data class Data(

	@field:SerializedName("ktp")
	val ktp: Ktp? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("provinsi")
	val provinsi: String? = null,

	@field:SerializedName("kota")
	val kota: String? = null,

	@field:SerializedName("ktpUrl")
	val ktpUrl: String? = null,

	@field:SerializedName("agama")
	val agama: String? = null,

	@field:SerializedName("ttl")
	val ttl: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("kewarganegaraan")
	val kewarganegaraan: String? = null,

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("kel_desa")
	val kelDesa: String? = null,

	@field:SerializedName("pekerjaan")
	val pekerjaan: String? = null,

	@field:SerializedName("validated")
	val validated: Boolean? = null,

	@field:SerializedName("kecamatan")
	val kecamatan: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null,

	@field:SerializedName("rt_rw")
	val rtRw: String? = null,

	@field:SerializedName("status_perkawinan")
	val statusPerkawinan: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
