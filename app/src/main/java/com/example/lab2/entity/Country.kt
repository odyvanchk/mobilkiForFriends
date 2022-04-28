package com.example.lab2.entity

enum class Country(value : String) {
    BELARUS("Belarus"),
    USA("USA"),
    UK("UK")
}

val COUNTRIES = mapOf(
    Country.BELARUS to "Belarus",
    Country.UK to "UK",
    Country.USA to "USA"
)