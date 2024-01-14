package edu.ufp.pam.wellbeing.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurvey(survey: Survey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: Answer)

    // Additional DAO methods for other entities like Answer if needed
}

