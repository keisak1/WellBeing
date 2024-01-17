package edu.ufp.pam.wellbeing.data.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ufp.pam.wellbeing.R

class SurveyAdapter(private val surveyItems: List<Question>) :
    RecyclerView.Adapter<SurveyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionText: TextView = itemView.findViewById(R.id.questionText)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)

    }

    val ratings = MutableList(surveyItems.size) { 0f }
    val questionsId: MutableList<Int> = surveyItems.map { it.id }.toMutableList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_survey, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = surveyItems[position]

            holder.questionText.text = item.text
        holder.ratingBar.numStars = item.intMax

        holder.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            ratings[position] = rating
        }

    }

    override fun getItemCount(): Int {
        return surveyItems.size
    }
}