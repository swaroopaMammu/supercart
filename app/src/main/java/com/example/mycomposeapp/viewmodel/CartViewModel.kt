package com.example.mycomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapp.model.GroceryModel
import com.example.mycomposeapp.model.db.entity.CartItemEntity
import com.example.mycomposeapp.model.repository.CartRepository
import com.example.mycomposeapp.utils.CommonUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartViewModel (val repository: CartRepository) : ViewModel() {


    lateinit var groceryMList : Flow<List<CartItemEntity>>
    lateinit var cTotal : Flow<Double>
    lateinit var pTotal : Flow<Double>
    private val _groceryMonthlyList = MutableStateFlow<List<CartItemEntity>>(emptyList())
    val groceryMonthlyList: StateFlow<List<CartItemEntity>> = _groceryMonthlyList.asStateFlow()


    fun addToCart(itemEntity: CartItemEntity){
        viewModelScope.launch {
            repository.insertNewCartItem(itemEntity)
        }
    }

    fun updateCartItem(item: GroceryModel, date:String){
        viewModelScope.launch {
            repository.updateCartItem(item,date)
        }
    }

    fun getCartItems(mDate:String){
        groceryMList =  repository.getCartItems(mDate)
    }

    fun getMonthlyCartItems(mDate:String) {
        val date = CommonUtils.getMonthYearFromDate(mDate)
        viewModelScope.launch {
            repository.getMonthlyCartItems(date)
                .collectLatest { cartItems ->
                    _groceryMonthlyList.value = cartItems
                    cartItems.forEach { item ->

                    }
                }
        }
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