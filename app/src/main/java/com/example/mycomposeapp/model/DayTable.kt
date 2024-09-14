package com.example.mycomposeapp.model


data class DayTable(
    val dId:String,
    val cartItemList:List<CartItemEntity>,
    val totalExp:Double,
    val mostExpItem:String
)
