package edu.ufp.pam.wellbeing.data

import androidx.room.TypeConverter
import edu.ufp.pam.wellbeing.data.model.Question

class Converters {

    @TypeConverter
    fun fromJson(json: String?): List<Question> {
        if (json == null) {
            return emptyList()
        }
        val type = object : TypeToken<List<Question>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: List<Question>): String {
        return Gson().toJson(list)
    }
}