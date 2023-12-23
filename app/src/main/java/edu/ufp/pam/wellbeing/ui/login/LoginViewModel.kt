package edu.ufp.pam.wellbeing.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ufp.pam.wellbeing.data.LoginRepository

import edu.ufp.pam.wellbeing.R
import edu.ufp.pam.wellbeing.data.model.User

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(user: User) {
        val result = loginRepository.login(user)
        Log.d("LOGINRESULT",result.toString())

        if (user.displayName.length >= 5) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = user.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return (username.isNotEmpty() || username.isNotBlank())
    }

}