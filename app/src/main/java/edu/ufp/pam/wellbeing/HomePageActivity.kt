package edu.ufp.pam.wellbeing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import edu.ufp.pam.wellbeing.data.AppDatabase
import edu.ufp.pam.wellbeing.data.SurveyRepository
import edu.ufp.pam.wellbeing.data.model.Survey
import edu.ufp.pam.wellbeing.data.model.SurveyDao
import edu.ufp.pam.wellbeing.databinding.ActivityHomePageBinding
import edu.ufp.pam.wellbeing.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomePageActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHomePage.toolbar)


        val selectedSurveys: List<Survey> = SurveyRepository.getSurveys()
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navView.menu.clear()
        val navController = findNavController(R.id.nav_host_fragment_content_home_page)


        val staticItems = setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
        val allDestinations = staticItems.toMutableSet()


        selectedSurveys.forEachIndexed { index, survey ->
            val menuItemId = index + 1000
            val menuItem =
                navView.menu.add(R.id.nav_slideshow, menuItemId, Menu.NONE, survey.title)
            menuItem.icon =
                ContextCompat.getDrawable(this@HomePageActivity, R.drawable.ic_menu_gallery)
            menuItem.setOnMenuItemClickListener {
                navController.navigate(R.id.nav_gallery, bundleOf("surveyId" to survey.id, "username" to intent.getStringExtra("username")))
                true
            }
            allDestinations.add(menuItemId)
        }

        Log.d("DRAWER", allDestinations.toString())
        appBarConfiguration = AppBarConfiguration(
            allDestinations, drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val headerView = navView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.username_header)
        usernameTextView.text = intent.getStringExtra("username")
    }


    /** private fun addFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.dynamic_fragment_container, fragment)
    transaction.addToBackStack(null) // Optional: Add to back stack if needed
    transaction.commit()
    }*/

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

}