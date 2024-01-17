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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


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


suspend fun insertSurveysAndAnswersIntoDatabase(context: Context) {
    val database = AppDatabase.getDatabase(context)
    val surveyDao = database.surveyDao()

    // Function to check if a survey with a given identifier exists in the database
    suspend fun getSurveyByIdentifier(title: String): Survey? {
        return withContext(Dispatchers.IO) {
            surveyDao.getSurveyByIdentifier(title)
        }
    }

    // Insert Sleep Survey if not already present
    if (getSurveyByIdentifier(sleepSurvey.title) == null) {
        surveyDao.insertSurvey(sleepSurvey)
        sleepSurvey.questions.forEach { question ->
            surveyDao.insertQuestion(question)
        }
    }

    // Insert Wellbeing1 Survey if not already present
    if (getSurveyByIdentifier(wellbeing1Survey.title) == null) {
        surveyDao.insertSurvey(wellbeing1Survey)
        wellbeing1Survey.questions.forEach { question ->
            surveyDao.insertQuestion(question)
        }
    }

    // Insert Wellbeing2 Survey if not already present
    if (getSurveyByIdentifier(wellbeing2Survey.title) == null) {
        surveyDao.insertSurvey(wellbeing2Survey)
        wellbeing2Survey.questions.forEach { question ->
            surveyDao.insertQuestion(question)
        }
    }
}