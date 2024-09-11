package com.example.mycomposeapp.model.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mycomposeapp.model.db.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert
    suspend fun insertNewCartItem(item: CartItemEntity)

    @Delete
    fun removeCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_table WHERE date = :mDate")
    fun removeDayCart(mDate: String)

    @Update
    fun updateCartItem(item: CartItemEntity)

    @Query("SELECT * FROM cart_table WHERE date = :mDate ")
    fun getCartItems(mDate: String): Flow<List<CartItemEntity>>

}