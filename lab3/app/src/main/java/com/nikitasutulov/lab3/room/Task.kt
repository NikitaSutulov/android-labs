package com.nikitasutulov.lab3.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (
    @PrimaryKey val id: Int,
    val text: String,
    val complexity: String,
    val type: String
)