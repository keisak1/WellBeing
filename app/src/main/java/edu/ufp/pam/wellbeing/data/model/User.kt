package edu.ufp.pam.wellbeing.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Entity(tableName = "user")
data class User(
    val displayName: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
