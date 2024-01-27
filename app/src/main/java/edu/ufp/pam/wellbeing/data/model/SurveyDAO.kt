package edu.ufp.pam.wellbeing.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SurveyDao {

    @Query("SELECT * FROM surveys WHERE title = :title")
    suspend fun getSurveyByIdentifier(title: String): Survey?

    @Query("SELECT * FROM surveys WHERE id = :id")
    fun getSurveyById(id: Int): Survey?

    @Query("SELECT * FROM questions WHERE surveyId = :id")
    fun getQuestions(id:Int):List<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurvey(survey: Survey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: Answer)

    @Insert
    suspend fun insertResult(result: ResultQuestions)

    @Query("SELECT * FROM surveys")
    fun getAllSurveys(): List<Survey>

    @Query("SELECT * FROM results")
    fun getAllResults(): List<ResultQuestions>

    @Query("DELETE FROM results")
    suspend fun deleteAllResults()
}

