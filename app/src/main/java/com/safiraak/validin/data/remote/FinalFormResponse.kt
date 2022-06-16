package com.safiraak.validin.data.remote

import com.google.gson.annotations.SerializedName

data class FinalFormResponse(

	@field:SerializedName("data")
	val data: DataFinal
)

data class DataFinal(

	@field:SerializedName("provinsi")
	val provinsi: String,

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("ktpUrl")
	val ktpUrl: String,

	@field:SerializedName("agama")
	val agama: String,

	@field:SerializedName("ttl")
	val ttl: String,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("kewarganegaraan")
	val kewarganegaraan: String,

	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("nik")
	val nik: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("kel_desa")
	val kelDesa: String,

	@field:SerializedName("pekerjaan")
	val pekerjaan: String,

	@field:SerializedName("validated")
	val validated: Boolean,

	@field:SerializedName("kecamatan")
	val kecamatan: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String,

	@field:SerializedName("rt_rw")
	val rtRw: String,

	@field:SerializedName("status_perkawinan")
	val statusPerkawinan: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
