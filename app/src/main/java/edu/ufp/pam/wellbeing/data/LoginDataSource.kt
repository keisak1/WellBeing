package edu.ufp.pam.wellbeing.data

import android.app.Application
import android.util.Log
import edu.ufp.pam.wellbeing.data.model.User
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(application : Application) {
    private val database = UserDataBase.getInstance(application)

    fun login(user: User): Result<User> {
        return try {
            database.insert(user)
            Log.d("DATABASE","Inserting in database")
            Result.Success(user)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}