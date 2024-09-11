package com.example.mycomposeapp.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cart_table")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val isPurChanged : Boolean,
    val title : String,
    val quantity : String,
    val cash : String,
    val date : String
)
