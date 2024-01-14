package edu.ufp.pam.wellbeing.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ufp.pam.wellbeing.data.model.Answer
import edu.ufp.pam.wellbeing.data.model.Question
import edu.ufp.pam.wellbeing.data.model.Survey
import edu.ufp.pam.wellbeing.data.model.SurveyDao
import kotlinx.coroutines.runBlocking


val sleepSurvey = Survey(
    title = "Sleep Quality",
    description = "Collect information about the quality of sleep and feeling of tiredness after waking up.",
    questions = listOf(
        Question(
            surveyId = 1,
            text = "I slept very well and feel that my sleep was totally restorative.",
            type = "Scale",
            intMin = 1,
            intMax = 3
        ),
        Question(
            surveyId = 1,
            text = "I feel totally rested after this night's sleep.",
            type = "Scale",
            intMin = 1,
            intMax = 3
        )
    ),
    category = "Sleep"
)

val wellbeing1Survey = Survey(
    title = "Wellbeing After Working Day",
    description = "Collect information about the feeling of well-being after the working day.",
    questions = listOf(
        Question(
            surveyId = 2,
            text = "I related easily to the people around me.",
            type = "Scale",
            intMin = 1,
            intMax = 3
        ),
        Question(
            surveyId = 2,
            text = "I was able to face difficult situations in a positive way.",
            type = "Scale",
            intMin = 1,
            intMax = 3
        ),
        Question(
            surveyId = 2,
            text = "I felt that others liked me and appreciated me.",
            type = "Scale",
            intMin = 1,
            intMax = 3
        ),
        Question(
            surveyId = 2,
            text = "I felt satisfied with what I was able to achieve, I felt proud of myself.",
            type = "Scale",
            intMin = 1,
            intMax = 3
        ),
        Question(
            surveyId = 2,
            text = "My life was well balanced between my family, personal and academic activities.",
            type = "Scale",
            intMin = 1,
            intMax = 3
        )
    ),
    category = "Wellbeing"
)

val wellbeing2Survey = Survey(
    title = "Wellbeing Anytime",
    description = "The user can answer this questionnaire whenever they want.",
    questions = listOf(
        Question(
            surveyId = 3,
            text = "I felt emotionally balanced.",
            type = "Scale",
            intMin = 1,
            intMax = 3
        ),
        Question(
            surveyId = 3,
            text = "I felt good, at peace with myself.",
            type = "Scale",
            intMin = 1,
            intMax = 3
        ),
        Question(
            surveyId = 3,
            text = "I felt confident.",
            type = "Scale",
            intMin = 1,
            intMax = 3
        )
    ),
    category = "Wellbeing"
)


@Database(entities = [Survey::class, Question::class, Answer::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class) // Add this line to include your TypeConverters

abstract class AppDatabase : RoomDatabase() {

    abstract fun surveyDao(): SurveyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wellbeing"
                )            .fallbackToDestructiveMigration() // Use with caution
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun initializeDatabase(context: Context) {
            val database = getDatabase(context)
            val surveyDao = database.surveyDao()

            runBlocking {
                insertSurveysAndAnswersIntoDatabase(context)
            }
        }
    }
}


// Inserting the surveys and answers into the database
suspend fun insertSurveysAndAnswersIntoDatabase(context: Context) {
    val database = AppDatabase.getDatabase(context)
    val surveyDao = database.surveyDao()

    // Insert Sleep Survey
    surveyDao.insertSurvey(sleepSurvey)
    sleepSurvey.questions.forEach { question ->
        surveyDao.insertQuestion(question)
    }

    // Sample Answers for Sleep Survey
   // surveyDao.insertAnswer(Answer(surveyId = sleepSurvey.id, questionId = sleepSurvey.questions[0].id, answerText = "2"))
   // surveyDao.insertAnswer(Answer(surveyId = sleepSurvey.id, questionId = sleepSurvey.questions[1].id, answerText = "3"))

    // Insert Wellbeing1 Survey
    surveyDao.insertSurvey(wellbeing1Survey)
    wellbeing1Survey.questions.forEach { question ->
        surveyDao.insertQuestion(question)
    }

    // Sample Answers for Wellbeing1 Survey
   // surveyDao.insertAnswer(Answer(surveyId = wellbeing1Survey.id, questionId = wellbeing1Survey.questions[0].id, answerText = "3"))
   // surveyDao.insertAnswer(Answer(surveyId = wellbeing1Survey.id, questionId = wellbeing1Survey.questions[1].id, answerText = "2"))
    // ... Repeat for other questions

    // Insert Wellbeing2 Survey
    surveyDao.insertSurvey(wellbeing2Survey)
    wellbeing2Survey.questions.forEach { question ->
        surveyDao.insertQuestion(question)
    }

    // Sample Answers for Wellbeing2 Survey
   // surveyDao.insertAnswer(Answer(surveyId = wellbeing2Survey.id, questionId = wellbeing2Survey.questions[0].id, answerText = "1"))
   // surveyDao.insertAnswer(Answer(surveyId = wellbeing2Survey.id, questionId = wellbeing2Survey.questions[1].id, answerText = "3"))
    // ... Repeat for other questions
}