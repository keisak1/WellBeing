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

    suspend fun login(user: User): Int {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("DATABASE", "Trying to insert into DB")
                database.insert(user)
                Log.d("DATABASE", "Inserted in database")
                return@withContext 0
            } catch (e: IOException) {
                Log.d("DATABASEERROR", e.message.toString())
                return@withContext 1
            }
        }
    }
    fun logout() {
        // TODO: revoke authentication
    }
}