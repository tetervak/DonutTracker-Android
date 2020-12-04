package com.android.samples.donuttracker.domain

import java.util.*

data class Donut(
    val id: String?,
    val name: String,
    val description: String,
    val rating: Int,
    val lowFat: Boolean = false,
    var date: Date? = Date()
)