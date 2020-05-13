package com.androidonlinux.checkboxcounter

data class Thing(
    val id: Int,
    val title: String = "",
    var isSelected: Boolean = false
) {

    fun toggleSelection() {
        isSelected = !isSelected
    }
}