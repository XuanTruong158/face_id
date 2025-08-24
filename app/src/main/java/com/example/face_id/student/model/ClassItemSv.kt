package com.example.face_id.model

import java.time.LocalDate
import java.time.LocalTime

data class ClassItemSv(
    val title: String,
    val code: String,
    val room: String,
    val date: LocalDate,
    val start: LocalTime,
    val lecturer: String
)
