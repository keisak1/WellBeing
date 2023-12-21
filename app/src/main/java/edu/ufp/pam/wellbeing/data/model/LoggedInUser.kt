package edu.ufp.pam.wellbeing.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val id: String,
    val displayName: String
)