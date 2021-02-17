package com.androidonlinux.checkboxcounter

data class Thing(
    val id: Long,
    val title: String = "",
    var isSelected: Boolean = false,
    var progress: Int? = null
)