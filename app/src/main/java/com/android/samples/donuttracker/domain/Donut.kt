package com.android.samples.donuttracker.domain

import java.util.*

data class Donut(
    val id: String?,
    var name: String = "",
    var description: String = "",
    val rating: Float = 3F,
    val lowFat: Boolean = false,
    var date: Date? = Date()
)