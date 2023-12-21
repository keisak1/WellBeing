package edu.ufp.pam.wellbeing

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import edu.ufp.pam.wellbeing.ui.login.LoginActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
}
