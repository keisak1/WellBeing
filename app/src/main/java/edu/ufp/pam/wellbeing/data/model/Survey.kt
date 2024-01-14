package edu.ufp.pam.wellbeing.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surveys")
data class Survey(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val questions: List<Question>,
    val category: String
)

