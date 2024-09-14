package com.example.mycomposeapp.utils
import androidx.room.TypeConverter
import com.example.mycomposeapp.model.CartItemEntity
import com.example.mycomposeapp.model.DayTable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    // Convert List<CartItemEntity> to JSON
    @TypeConverter
    fun fromCartItemList(cartItems: List<CartItemEntity>): String {
        return Gson().toJson(cartItems)
    }

    // Convert JSON to List<CartItemEntity>
    @TypeConverter
    fun toCartItemList(cartItemsString: String): List<CartItemEntity> {
        val listType = object : TypeToken<List<CartItemEntity>>() {}.type
        return Gson().fromJson(cartItemsString, listType)
    }

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