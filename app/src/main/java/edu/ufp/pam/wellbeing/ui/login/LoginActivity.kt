package edu.ufp.pam.wellbeing.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import edu.ufp.pam.wellbeing.HomePageActivity
import edu.ufp.pam.wellbeing.databinding.ActivityLoginBinding

import edu.ufp.pam.wellbeing.R
import edu.ufp.pam.wellbeing.data.model.User
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        val factory = LoginViewModelFactory.createFactory(application)

        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless username is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            Log.d("teste","Trying to login")
            loading.visibility = View.VISIBLE

            if(loginResult.error != null){
                showLoginFailed(R.string.wrong_password)
                Log.d("CHECK","Failed")
            }

            if (username.text.toString().length < 5 || password?.text.toString().length < 5) {
                showLoginFailed(R.string.login_failed)
                loading.visibility = View.GONE
                Log.d("CHECK","Failed")

            }
            if (username.text.toString().length >= 5 && password?.text.toString().length >= 5 ) {
                updateUiWithUser(loginResult.success!!)
                Log.d("CHECK","Passed everything")

                val intent = Intent(this, HomePageActivity::class.java)

                intent.putExtra("username", username.text.toString())

                startActivity(intent)
                finish()

            }
            setResult(Activity.RESULT_OK)



        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString()
            )
        }
        login.setOnClickListener {
            val newUser = User(username.text.toString(), password?.text.toString())

            lifecycleScope.launch {
                // Call the login function from within a coroutine
                loginViewModel.login(newUser)
            }
        }




    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
