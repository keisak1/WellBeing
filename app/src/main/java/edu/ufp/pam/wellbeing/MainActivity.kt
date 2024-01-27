package edu.ufp.pam.wellbeing

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import edu.ufp.pam.wellbeing.data.AppDatabase
import edu.ufp.pam.wellbeing.data.SurveyRepository
import edu.ufp.pam.wellbeing.data.model.ResultQuestions
import edu.ufp.pam.wellbeing.data.model.Survey
import edu.ufp.pam.wellbeing.data.model.SurveyDao
import edu.ufp.pam.wellbeing.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

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
        lifecycleScope.launch(Dispatchers.IO) {
            while (isActive) { // Continuously run the loop while the coroutine is active
                Log.d("Internet", "Checking for internet")
                if (isNetworkAvailable(this@MainActivity)) {
                    Log.d("Internet", "Internet available, sending results")
                    checkAndSendResults()
                } else {
                    Log.d("Internet", "No internet available")
                }
                delay(60000) // Delay for 1 minute before checking again (adjust as needed)
            }
        }

    }


    private fun checkAndSendResults() {
        lifecycleScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(applicationContext)
            val surveyDao = database.surveyDao()
            val selectedSurveys: List<ResultQuestions> = surveyDao.getAllResults()

            // Check for internet connectivity
            if (isNetworkAvailable(this@MainActivity)) {
                Log.d("Internet","Internet available")
                sendResults(selectedSurveys, surveyDao)
            } else {
                // Handle no internet connectivity
                Log.d("Internet", "No internet connection available")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private suspend fun sendResults(surveys: List<ResultQuestions>, surveyDao: SurveyDao) {
        if (surveys.isNotEmpty()) {
            Log.d("Internet", "Connecting to server...")

            val url = URL("http://192.168.1.18:8080/")
            val connection = withContext(Dispatchers.IO) {
                url.openConnection()
            } as HttpURLConnection

            try {
                // Set up the connection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

                Log.d("Internet", "Preparing JSON...")

                // Prepare the survey data to send
                val surveyJsonArray = JSONArray()
                Log.d("Internet", surveys.toString())
                for (survey in surveys) {
                    val surveyJson = JSONObject()
                    surveyJson.put("Id", survey.id)
                    surveyJson.put("surveyId", survey.surveyId)
                    surveyJson.put("questionId", survey.questionId)
                    surveyJson.put("questionRate", survey.questionRate)
                    surveyJson.put("userId", survey.userId)
                    surveyJson.put("date", survey.date)
                    surveyJsonArray.put(surveyJson)
                }

                Log.d("Internet", "Writing to output...")

                // Write the survey data to the connection output stream
                val outputStream = connection.outputStream
                withContext(Dispatchers.IO) {
                    outputStream.write(surveyJsonArray.toString().toByteArray(Charsets.UTF_8))
                }
                withContext(Dispatchers.IO) {
                    outputStream.close()
                }

                // Get the response from the server
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Survey results sent successfully, handle accordingly
                    Log.d("Internet", "Survey results sent successfully")
                    surveyDao.deleteAllResults()
                } else {
                    // Handle error response from server
                    Log.e("Internet", "Failed to send survey results. Response code: $responseCode")
                }
            } catch (e: Exception) {
                // Handle exception
                Log.e("Internet", "Error sending survey results", e)
            } finally {
                // Disconnect the connection
                connection.disconnect()
            }
        }
    }
}
