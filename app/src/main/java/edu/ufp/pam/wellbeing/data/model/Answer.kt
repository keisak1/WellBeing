package edu.ufp.pam.wellbeing.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "answers")
data class Answer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surveyId: Int,
    val questionId: Int,
    val answerText: String
)
