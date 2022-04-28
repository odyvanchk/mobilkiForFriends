package com.example.lab2.entity

enum class Sex(value : String) {
    MALE("Male"),
    FEMALE("Female")
}

val SEX = mapOf(
    Sex.MALE to "Male",
    Sex.FEMALE to "Female"
)