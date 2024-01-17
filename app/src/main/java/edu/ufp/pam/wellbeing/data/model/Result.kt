package edu.ufp.pam.wellbeing.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "results")
data class Result(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val questionId: Int,
    val questionRate: Float,
    val date : Date
)
