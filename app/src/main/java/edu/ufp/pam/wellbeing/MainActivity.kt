package edu.ufp.pam.wellbeing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import edu.ufp.pam.wellbeing.data.AppDatabase
import edu.ufp.pam.wellbeing.data.SurveyRepository
import edu.ufp.pam.wellbeing.data.model.Survey
import edu.ufp.pam.wellbeing.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, LoginActivity::class.java)

        // Initialize the database
        AppDatabase.initializeDatabase(this)
        lifecycleScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(applicationContext)
            val surveyDao = database.surveyDao()
            val selectedSurveys: List<Survey> = surveyDao.getAllSurveys()
            SurveyRepository.setSurveys(selectedSurveys)

            Log.d("DRAWER", selectedSurveys.toString())
        }
        startActivity(intent)

    }
}
