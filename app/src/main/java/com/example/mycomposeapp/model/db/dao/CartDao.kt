package com.example.mycomposeapp.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mycomposeapp.model.db.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert
    suspend fun insertNewCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_table WHERE id = :mId")
    suspend fun removeCartItem(mId:Int)

    @Query("DELETE FROM cart_table WHERE date = :mDate")
    suspend fun removeDayCart(mDate: String)

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Query("SELECT * FROM cart_table WHERE date = :mDate ")
    fun getCartItems(mDate: String): Flow<List<CartItemEntity>>

    @Query("SELECT IFNULL(SUM(CAST(cash AS REAL)), 0) FROM cart_table WHERE date = :mDate")
    fun getCartTotal(mDate: String): Flow<Double>

    @Query("SELECT IFNULL(SUM(CAST(cash AS REAL)), 0) FROM cart_table WHERE date = :mDate AND isPurChanged = 1")
    fun getPurchasedTotal(mDate: String): Flow<Double>

    @Query("SELECT * FROM cart_table WHERE date LIKE '%' || :mDate")
    fun getMonthlyCartItems(mDate: String): Flow<List<CartItemEntity>>

}