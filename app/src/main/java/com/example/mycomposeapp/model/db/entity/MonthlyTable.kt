package com.example.mycomposeapp.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_cart")
data class MonthlyTable(
    @PrimaryKey
    val mId:String,
    val dayCartList: String,
    val mostExpDay : String,
    val mostExpItem :String,
    val mostBought :String
)
