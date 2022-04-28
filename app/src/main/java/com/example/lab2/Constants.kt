package com.example.lab2

class Constants {
    companion object {
        val GRID = 0
        val LIST = 1

        val ACTIONS = mapOf(
            Actions.EDIT to "edit",
            Actions.ADD to "add",
        )
    }
}

enum class Actions {
    ADD, EDIT
}
