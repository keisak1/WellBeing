package edu.ufp.pam.wellbeing.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ufp.pam.wellbeing.HomePageActivity
import edu.ufp.pam.wellbeing.R
import edu.ufp.pam.wellbeing.data.AppDatabase
import edu.ufp.pam.wellbeing.data.UserDataBase
import edu.ufp.pam.wellbeing.data.model.Result
import edu.ufp.pam.wellbeing.data.model.Survey
import edu.ufp.pam.wellbeing.data.model.SurveyAdapter
import edu.ufp.pam.wellbeing.data.model.User
import edu.ufp.pam.wellbeing.databinding.FragmentGalleryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null


    private val binding get() = _binding!!
    private var survey : Survey? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var surveyAdapter: SurveyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        arguments?.let { bundle ->
            val surveyId = bundle.getInt("surveyId", /* default value if not found */ -1)
            if (surveyId != -1) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val database = context?.let { AppDatabase.getDatabase(it) }
                    val surveyDao = database?.surveyDao()

                    val survey = withContext(Dispatchers.IO) {
                        surveyDao?.getSurveyById(surveyId)
                    }

                    if (survey != null) {
                        val textView: TextView = binding.textGallery
                        galleryViewModel.text.observe(viewLifecycleOwner) {
                            textView.text = survey.title
                        }
                        recyclerView = binding.questionRecycler
                        surveyAdapter = SurveyAdapter(survey.questions)
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter = surveyAdapter


                    }
                }
            }
        }


        val results: MutableList<Result> = mutableListOf()
        val btnGetResults: Button = binding.button
        btnGetResults.setOnClickListener {
            val adapter = recyclerView.adapter as SurveyAdapter
            val ratings = adapter.ratings
            val database = context?.let { AppDatabase.getDatabase(it) }
            val surveyDao = database?.surveyDao()
            val userdatabase = context?.let { UserDataBase.getInstance(it) }
            val userDao = userdatabase!!.userDao()
            val intent = Intent(this, HomePageActivity::class.java)
            userDao.getUserByDisplayName()
            intent.getStringExtra("username")

            Log.d("Ratings", ratings.toString())
            findNavController().navigate(R.id.nav_home)
            Toast.makeText(requireContext(), "Survey done!", Toast.LENGTH_SHORT).show()

            adapter.questions.forEach({question ->
                Result()

            })
        }

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}