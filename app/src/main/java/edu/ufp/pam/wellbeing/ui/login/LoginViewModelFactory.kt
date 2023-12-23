package edu.ufp.pam.wellbeing.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.ufp.pam.wellbeing.data.LoginDataSource
import edu.ufp.pam.wellbeing.data.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    companion object {
        fun createFactory(application: Application): LoginViewModelFactory {
            return LoginViewModelFactory(application)
        }
    }
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(application)
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}