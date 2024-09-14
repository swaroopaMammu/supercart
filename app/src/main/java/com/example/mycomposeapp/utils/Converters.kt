package com.example.mycomposeapp.utils
import androidx.room.TypeConverter
import com.example.mycomposeapp.model.DayTable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    // Convert List<DayTable> to JSON
    @TypeConverter
    fun fromDayTableList(dayTables: List<DayTable>): String {
        return Gson().toJson(dayTables)
    }

    // Convert JSON to List<DayTable>
    @TypeConverter
    fun toDayTableList(dayTablesString: String): List<DayTable> {
        val listType = object : TypeToken<List<DayTable>>() {}.type
        return Gson().fromJson(dayTablesString, listType)
    }
}