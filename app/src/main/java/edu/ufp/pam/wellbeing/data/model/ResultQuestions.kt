package edu.ufp.pam.wellbeing.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "results")
data class ResultQuestions(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val surveyId: Int,
    val questionId: Int,
    val questionRate: Float,
    val date: LocalDateTime
)
