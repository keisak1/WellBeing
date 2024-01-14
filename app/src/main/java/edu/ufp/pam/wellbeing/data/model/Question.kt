package edu.ufp.pam.wellbeing.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surveyId: Int,
    val text: String,
    val type: String,
    val scale: IntRange,
)




