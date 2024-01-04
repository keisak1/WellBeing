package edu.ufp.pam.wellbeing.data

import android.util.Log
import edu.ufp.pam.wellbeing.data.model.User

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(user: User): User{

        when (val result = dataSource.login(user)) {
            is Result.UserInserted -> {
                setLoggedInUser(user)
                Log.d("DATABASE", "User inserted into DB. UserId: $user.id")
                return user
            }
            is Result.UserExists -> {
                Log.d("DATABASE", "User already exists, attempting to login with credentials")
                if (result.user != null) {
                    setLoggedInUser(user)
                    return user
                }
                return User("1", "1")
            }
            is Result.DatabaseError -> {
                Log.d("DATABASEERROR", result.errorMessage)
                throw Exception("Database error")

            }

            else -> {
                Log.d("DATABASEERROR", "Unexpected result type: $result")
                throw Exception(result.toString())

            }
        }

    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}