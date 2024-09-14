package com.example.mycomposeapp.model

import com.example.mycomposeapp.utils.AppConstants

data class GroceryModel(
    val id:Int = 0,
    val isPurChanged : Boolean = false,
    val title : String = AppConstants.EMPTY,
    val quantity : String = AppConstants.EMPTY,
    val cash : String = AppConstants.EMPTY
)
