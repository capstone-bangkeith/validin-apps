package com.safiraak.validin.data

import android.graphics.RectF

data class RecognitionData(val label: String, val confidence: Float, val location: RectF) {
    override fun toString(): String {
        return "$label / $probabilityString"
    }
    val probabilityString = String.format("%.1f%%", confidence*100.0f)
}