package com.example.mycomposeapp.model.repository

import com.example.mycomposeapp.model.GroceryModel
import com.example.mycomposeapp.model.db.dao.CartDao
import com.example.mycomposeapp.model.db.entity.CartItemEntity

class CartRepository(private val cartDao: CartDao){

    suspend fun insertNewCartItem(item: CartItemEntity){
         cartDao.insertNewCartItem(item)
    }

    suspend fun removeCartItem(id:Int){
        cartDao.removeCartItem(id)
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

    fun getCartTotal(date:String) =
        cartDao.getCartTotal(date)

     fun getPurchasedTotal(date:String) = cartDao.getPurchasedTotal(date)

     fun getCartItems(mDate:String) = cartDao.getCartItems(mDate)

     fun getMonthlyCartItems(mDate:String) = cartDao.getMonthlyCartItems(mDate)

}