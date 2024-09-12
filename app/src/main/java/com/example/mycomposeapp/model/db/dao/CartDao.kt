package com.example.mycomposeapp.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mycomposeapp.model.db.entity.MonthlyTable
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    // Insert Monthly Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthlyTable(monthlyTable: MonthlyTable)

    @Update
    suspend fun updateMonthlyTable(monthlyTable: MonthlyTable)

    @Query("SELECT COUNT(*) FROM monthly_cart WHERE mId = :id")
    suspend fun isCartItemExists(id: String): Int

    @Query("SELECT * FROM monthly_cart WHERE mId = :date")
    fun getMonthlyCartItems(date: String): Flow<MonthlyTable>

}