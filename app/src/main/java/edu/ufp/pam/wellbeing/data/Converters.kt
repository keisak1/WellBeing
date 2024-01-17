package edu.ufp.pam.wellbeing.data

import androidx.room.TypeConverter
import edu.ufp.pam.wellbeing.data.model.Question
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

object Converters {

    @TypeConverter
    fun fromJson(json: String?): List<Question> {
        return json?.let { Json.decodeFromString(it) } ?: emptyList()
    }

    @TypeConverter
    fun toJson(list: List<Question>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }

}