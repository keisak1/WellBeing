package edu.ufp.pam.wellbeing.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surveyId: Int,
    val text: String,
    val type: String,
    val scale: IntRange,
)


