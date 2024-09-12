package com.example.mycomposeapp.model.db.entity


data class CartItemEntity(
    val id:Int,
    val isPurChanged : Boolean,
    val title : String,
    val quantity : String,
    val cash : String,
)
