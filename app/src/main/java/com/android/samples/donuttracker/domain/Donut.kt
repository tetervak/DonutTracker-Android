package com.android.samples.donuttracker.domain

data class Donut(
    val id: Long,
    val name: String,
    val description: String,
    val rating: Int,
    val lowFat: Boolean = false
)