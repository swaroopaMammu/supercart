package com.example.mycomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapp.model.GroceryModel
import com.example.mycomposeapp.model.db.entity.CartItemEntity
import com.example.mycomposeapp.model.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CartViewModel (val repository: CartRepository) : ViewModel() {


    lateinit var groceryMList : Flow<List<CartItemEntity>>
    lateinit var cTotal : Flow<Double>
    lateinit var pTotal : Flow<Double>

    fun addToCart(itemEntity: CartItemEntity){
        viewModelScope.launch {
            repository.insertNewCartItem(itemEntity)
        }
    }

    fun updateCartItem(item: GroceryModel, date:String){
        viewModelScope.launch {
            println("grocery model: $item")
            repository.updateCartItem(item,date)
        }
    }

    fun getCartItems(mDate:String){
        groceryMList =  repository.getCartItems(mDate)
    }

    fun removeCartItem(id:Int){
        viewModelScope.launch {

            repository.removeCartItem(id)
        }
    }
    fun removeDayCart(date:String){
        viewModelScope.launch {
            repository.removeDayCart(date)
        }
    }

    fun getCartTotal(date:String){
            cTotal = repository.getCartTotal(date)
    }

    fun getPurchasedTotal(date:String){
          pTotal = repository.getPurchasedTotal(date)
    }

}