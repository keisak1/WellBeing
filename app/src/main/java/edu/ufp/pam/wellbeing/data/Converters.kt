package edu.ufp.pam.wellbeing.data

import androidx.room.TypeConverter
import edu.ufp.pam.wellbeing.data.model.Question
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object Converters {

    @TypeConverter
    fun fromJson(json: String?): List<Question> {
        return json?.let { Json.decodeFromString(it) } ?: emptyList()
    }

    @TypeConverter
    fun toJson(list: List<Question>): String {
        return Json.encodeToString(list)
    }

}