package com.example.mycomposeapp.model.db.entity


data class DayTable(
    val dId:String,
    val cartItemList:List<CartItemEntity>,
    val totalExp:Double,
    val mostExpItem:String
)
