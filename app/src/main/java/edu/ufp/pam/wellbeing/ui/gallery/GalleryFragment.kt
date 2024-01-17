package edu.ufp.pam.wellbeing.ui.gallery

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ufp.pam.wellbeing.R
import edu.ufp.pam.wellbeing.data.AppDatabase
import edu.ufp.pam.wellbeing.data.UserDataBase
import edu.ufp.pam.wellbeing.data.model.ResultQuestions
import edu.ufp.pam.wellbeing.data.model.Survey
import edu.ufp.pam.wellbeing.data.model.SurveyAdapter
import edu.ufp.pam.wellbeing.databinding.FragmentGalleryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null


    private val binding get() = _binding!!
    private var username = ""
    private var surveyId = -1
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
            surveyId = bundle.getInt("surveyId", /* default value if not found */ -1)
            username = bundle.getString("username", /* default value if not found */ "")

            if (surveyId != -1) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val database = context?.let { AppDatabase.getDatabase(it) }
                    val surveyDao = database?.surveyDao()

                    val survey = withContext(Dispatchers.IO) {
                        surveyDao?.getSurveyById(surveyId)
                    }

                    val questions = withContext(Dispatchers.IO){surveyDao?.getQuestions(surveyId)}
                    if (survey != null && questions != null) {
                        val textView: TextView = binding.textGallery
                        galleryViewModel.text.observe(viewLifecycleOwner) {
                            textView.text = survey.title
                        }
                        recyclerView = binding.questionRecycler
                        surveyAdapter = SurveyAdapter(questions)
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter = surveyAdapter


                    }
                }
            }
        }


        val results: MutableList<ResultQuestions> = mutableListOf()
        val btnGetResults: Button = binding.button
        btnGetResults.setOnClickListener {
            val adapter = recyclerView.adapter as SurveyAdapter
            val ratings = adapter.ratings
            val database = context?.let { AppDatabase.getDatabase(it) }
            val surveyDao = database?.surveyDao()
            val userdatabase = context?.let { UserDataBase.getInstance(it) }
            val userDao = userdatabase!!.userDao()

            Log.d("Ratings", ratings.toString())
            findNavController().navigate(R.id.nav_home)
            Toast.makeText(requireContext(), "Survey done!", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch(Dispatchers.IO) {
                val user = userDao.getUserByDisplayName(username)
                
                withContext(Dispatchers.Main) {
                    adapter.questionsId.forEachIndexed { index, question ->
                        val result = ResultQuestions(
                            userId = user!!.id,
                            surveyId = surveyId,
                            questionId = question,
                            questionRate = ratings[index],
                            date = LocalDateTime.now()
                        )
                        results.add(result)
                    }

                    results.forEach { result ->
                        surveyDao?.insertResult(result = result)
                    }

                }

            }
        }

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}