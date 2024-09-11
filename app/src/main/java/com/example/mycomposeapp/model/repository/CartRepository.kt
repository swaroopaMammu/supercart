package com.example.mycomposeapp.model.repository

import com.example.mycomposeapp.model.GroceryModel
import com.example.mycomposeapp.model.db.dao.CartDao
import com.example.mycomposeapp.model.db.entity.CartItemEntity

class CartRepository(private val cartDao: CartDao){

    suspend fun insertNewCartItem(item: CartItemEntity){
         cartDao.insertNewCartItem(item)
    }

    suspend fun removeCartItem(item: CartItemEntity){
        cartDao.removeCartItem(item)
    }

    suspend fun removeDayCart(mDate: String){
        cartDao.removeDayCart(mDate)
    }

    suspend fun updateCartItem(item: GroceryModel,date:String){
        val newItem = CartItemEntity(
            id = item.id,
            title = item.title,
            cash = item.cash,
            isPurChanged = item.isPurChanged,
            quantity = item.quantity,
            date = date
        )
        cartDao.updateCartItem(newItem)
    }

     fun getCartItems(mDate:String) = cartDao.getCartItems(mDate)

}