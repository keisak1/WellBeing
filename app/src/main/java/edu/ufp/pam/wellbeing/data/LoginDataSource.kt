package edu.ufp.pam.wellbeing.data

import android.app.Application
import android.util.Log
import edu.ufp.pam.wellbeing.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(application : Application) {
    private val database = UserDataBase.getInstance(application)

    suspend fun login(user: User): Result<Int> {
        return withContext(Dispatchers.IO) {
            try {
                if (database.getUser(user) == null) {
                    database.insert(user)
                    return@withContext Result.UserInserted(user.id)
                } else {
                    var existingUser: User? = null
                    if(database.getUserByCredentials(user) != null) {
                        existingUser = database.getUserByCredentials(user)
                    }
                    return@withContext Result.UserExists(existingUser)
                }
            } catch (e: IOException) {
                val errorMessage = "Database error: ${e.message}"
                Log.e("DATABASEERROR", errorMessage)
                return@withContext Result.DatabaseError(errorMessage)
            }
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}