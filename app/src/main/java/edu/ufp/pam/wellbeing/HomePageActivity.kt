package edu.ufp.pam.wellbeing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import edu.ufp.pam.wellbeing.data.AppDatabase
import edu.ufp.pam.wellbeing.data.model.Survey
import edu.ufp.pam.wellbeing.databinding.ActivityHomePageBinding
import edu.ufp.pam.wellbeing.ui.login.LoginActivity

class HomePageActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHomePage.toolbar)

        binding.appBarHomePage.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home_page)

        // Initialize the database
        AppDatabase.initializeDatabase(context = this.applicationContext)
        val selectedSurveys = getSelectedSurveys()

        // Clear existing menu items
        navView.menu.clear()

        // Add dynamically created menu items
        selectedSurveys.forEach { survey ->
            val menuItem = navView.menu.add(R.id.nav_gallery, Menu.NONE, Menu.NONE, survey.title)
            menuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_menu_gallery) // Replace with your survey icon
            menuItem.setOnMenuItemClickListener {
                // Handle item click here, for example, navigate to the corresponding survey fragment
                navController.navigate(R.id.nav_gallery, bundleOf("surveyId" to survey.id))
                true
            }
        }
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerView = navView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.username_header)
        usernameTextView.text = intent.getStringExtra("username")
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_page, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home_page)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Handle menu item selection in onOptionsItemSelected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                performLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performLogout() {
        // Clear user data, session, tokens, etc. (You will implement this next)

        // Navigate to LoginActivity
        navigateToLoginActivity()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity() // Optional: Close all previous activities
    }

    private fun getSelectedSurveys(): List<Survey> {
        // Fetch the list of surveys the user wants to display (from preferences, database, etc.)
        // Replace this with your actual implementation to get the list of surveys
        // For example, you can query the database or use SharedPreferences
        return listOf(

        )
    }
}