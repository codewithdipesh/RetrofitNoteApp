package com.example.notesappretrofit.utils

private var tempIdCounter = -1

fun generateTempId(): Int {
    tempIdCounter--
    return tempIdCounter
}