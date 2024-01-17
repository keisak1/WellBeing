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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurvey(survey: Survey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: Answer)

    @Insert
    suspend fun insertResult(result: Result)

    @Query("SELECT * FROM surveys")
    fun getAllSurveys(): List<Survey>
}

