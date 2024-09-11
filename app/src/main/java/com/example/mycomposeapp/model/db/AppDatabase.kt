package com.example.mycomposeapp.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mycomposeapp.model.db.dao.CartDao
import com.example.mycomposeapp.model.db.entity.CartItemEntity

@Database(entities = [CartItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun cartDao():CartDao

    companion object{
        private var Instance:AppDatabase? = null

        fun getInstance(context: Context):AppDatabase{
            synchronized(this){
                var instance = Instance
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "super_cart").build()
                    }
                    Instance = instance
                return instance
                }
            }
        }
    }