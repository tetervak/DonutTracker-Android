package com.android.samples.donuttracker.domain

import java.util.*
import java.io.Serializable

data class Donut(
    val id: String?,
    var name: String = "",
    var description: String = "",
    var rating: Int = 3,
    var lowFat: Boolean = false,
    var date: Date? = Date()
): Serializable