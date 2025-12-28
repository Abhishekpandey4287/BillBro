package com.example.billbro.utils

object NameNormalizer {
    fun normalize(name: String): String =
        name.trim()
            .lowercase(java.util.Locale.ROOT)
            .split(Regex("\\s+"))
            .joinToString(" ")
}