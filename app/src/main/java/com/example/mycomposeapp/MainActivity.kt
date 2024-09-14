package com.example.mycomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mycomposeapp.model.db.AppDatabase
import com.example.mycomposeapp.model.repository.CartRepository
import com.example.mycomposeapp.screens.HomeScreenUi
import com.example.mycomposeapp.ui.theme.MyComposeAppTheme
import com.example.mycomposeapp.viewmodel.CartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = AppDatabase.getInstance(context = applicationContext)
        val dayDao = db.cartDao()
        val repository = CartRepository(dayDao)
        val viewModel = CartViewModel(repository)
        setContent {
            MyComposeAppTheme {
               Surface(
                   modifier = Modifier.fillMaxSize(),
                   color = MaterialTheme.colorScheme.background
               ) {
                   HomeScreenUi(viewModel)
               }
            }
        }
    }
}
