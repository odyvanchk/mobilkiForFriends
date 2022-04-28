package com.example.lab2.entity

enum class Language(value: String) {
    ENGLISH("English"),
    POLISH("Polish"),
    GERMAN("German")
}
val LANGUAGES = mapOf(
    Language.ENGLISH to "English",
    Language.POLISH to "Polish",
    Language.GERMAN to "German"
)