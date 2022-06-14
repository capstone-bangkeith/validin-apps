package com.safiraak.validin.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class SetDataKtp(
    var nik: String?,
    var provinsi: String?,
    var kabupaten: String?,
    var kecamatan: String?,
    var nama: String?,
    var jk: String?,
    var ttl: String?,
    var agama: String?,
    var statPer: String?,
    var pekerjaan: String?,
    var kwn: String?,
    var url: String?,
    var rtrw: String?,
    var keldes: String?
    ) : Parcelable
