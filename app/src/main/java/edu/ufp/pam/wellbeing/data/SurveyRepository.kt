package edu.ufp.pam.wellbeing.data

import edu.ufp.pam.wellbeing.data.model.Survey


object SurveyRepository {
    private var surveys: List<Survey> = emptyList()

    fun setSurveys(surveys: List<Survey>) {
        this.surveys = surveys
    }

    fun getSurveys(): List<Survey> {
        return surveys
    }


}