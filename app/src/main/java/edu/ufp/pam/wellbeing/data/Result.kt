package edu.ufp.pam.wellbeing.data

import edu.ufp.pam.wellbeing.data.model.User

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class UserInserted(val userId: Int) : Result<Int>()
    data class UserExists(val user: User?) : Result<Nothing>()
    data class DatabaseError(val errorMessage: String) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is UserInserted -> "UserInserted[userId=$userId]"
            is UserExists -> "UserExists"
            is DatabaseError -> "DatabaseError[errorMessage=$errorMessage]"
        }
    }
}
