package com.example.mycomposeapp.model.repository

import com.example.mycomposeapp.model.db.dao.CartDao
import com.example.mycomposeapp.model.db.entity.MonthlyTable

class CartRepository(private val cartDao: CartDao){

    suspend fun insertMonthlyTable(item: MonthlyTable){
         cartDao.insertMonthlyTable(item)
    }

    suspend fun updateMonthlyTable(item: MonthlyTable){
        cartDao.updateMonthlyTable(item)
    }

    suspend fun isCartItemExists(id:String) = cartDao.isCartItemExists(id)

    fun getMonthlyCartItems(date:String) = cartDao.getMonthlyCartItems(date)

}