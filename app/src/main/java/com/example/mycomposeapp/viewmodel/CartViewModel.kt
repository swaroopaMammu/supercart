package com.example.mycomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapp.model.GroceryModel
import com.example.mycomposeapp.model.db.entity.CartItemEntity
import com.example.mycomposeapp.model.db.entity.DayTable
import com.example.mycomposeapp.model.db.entity.MonthlyTable
import com.example.mycomposeapp.model.repository.CartRepository
import com.example.mycomposeapp.utils.CommonUtils
import com.example.mycomposeapp.utils.Converters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel (val repository: CartRepository) : ViewModel() {


    private val _groceryEntityList = MutableStateFlow<List<CartItemEntity>>(emptyList())
    val groceryEntityList: StateFlow<List<CartItemEntity>> = _groceryEntityList.asStateFlow()

    lateinit var monthData:MonthlyTable

    val converters = Converters()
    val dayList = mutableListOf<DayTable>()
    val entityList = mutableListOf<CartItemEntity>()

    fun updateCartItem(item: GroceryModel, date:String){
        viewModelScope.launch {
            val dIterator = entityList.iterator()
            val entity = CartItemEntity(
                id = item.id,
                cash = item.cash,
                isPurChanged = item.isPurChanged,
                quantity = item.quantity,
                title = item.title
            )
            while (dIterator.hasNext()) {
                val d = dIterator.next()
                if (d.id == item.id) {
                    dIterator.remove()
                }
            }
            entityList.add(entity)

            val mIterator = dayList.iterator()
            while (mIterator.hasNext()) {
                val m = mIterator.next()
                if (m.dId == date) {
                    mIterator.remove()
                }
            }
            val updatedDayTable = DayTable(
                dId = date,
                cartItemList = converters.fromCartItemList(entityList),
                totalExp = 0.0
            )
            dayList.add(updatedDayTable)
            repository.updateMonthlyTable(MonthlyTable(
                mId = CommonUtils.getMonthYearFromDate(date),
                mostBought = "",
                mostExpItem = "",
                mostExpDay = "",
                dayCartList = converters.fromDayTableList(dayList)
            ))
        }
    }

    fun getCartItems(mDate:String){
            viewModelScope.launch {
                val date1 = CommonUtils.getMonthYearFromDate(mDate)
                repository.getMonthlyCartItems(date1).collectLatest {
                        cartItems ->
                    if(cartItems != null){
                        dayList.clear()
                        entityList.clear()
                        dayList.addAll(converters.toDayTableList(cartItems.dayCartList))
                        dayList.forEach{
                            if(it.dId == mDate){
                                entityList.addAll(converters.toCartItemList(it.cartItemList))
                            }
                        }
                        withContext(Dispatchers.Main) {
                            _groceryEntityList.value = entityList.toList()
                        }
                    }
                }
            }
    }

    fun updateMonthlyCartItems(date:String,model:GroceryModel) { // add new item
        val date1 = CommonUtils.getMonthYearFromDate(date)
        viewModelScope.launch {
            val count = repository.isCartItemExists(date1)
            if(count != 0){
                updateCartItem(item = model,date=date)
            }else{
                val mList = mutableListOf<DayTable>()
                val dList = mutableListOf<CartItemEntity>()
                dList.add(
                    CartItemEntity(
                        id = model.id,
                        title = model.title,
                        cash = model.cash,
                        isPurChanged = model.isPurChanged,
                        quantity = model.quantity
                    ))
                val dayData = DayTable(
                    dId = date,
                    totalExp = 0.0,
                    cartItemList = converters.fromCartItemList(dList)
                )
                mList.add(dayData)
                val data = MonthlyTable(
                    mId = date1,
                    mostBought = "",
                    mostExpItem = "",
                    mostExpDay = "",
                    dayCartList = converters.fromDayTableList(mList)
                )
                repository.insertMonthlyTable(data)
            }

        }
    }


    fun removeCartItem(mId:Int,date: String){
        viewModelScope.launch(Dispatchers.IO) {

            val dIterator =entityList.iterator()
            while(dIterator.hasNext()){
                val d = dIterator.next()
                if(d.id == mId){
                    dIterator.remove()
                }
            }
            val mIterator = dayList.iterator()
            while(mIterator.hasNext()){
                val m = mIterator.next()
                if(m.dId == date){
                    val copyM = DayTable(
                        dId = m.dId,
                        cartItemList = converters.fromCartItemList(entityList),
                        totalExp = 0.0
                    )
                    mIterator.remove()
                    dayList.add(copyM)
                }
            }
            repository.updateMonthlyTable(MonthlyTable(
                mId = CommonUtils.getMonthYearFromDate(date),
                mostBought = "",
                mostExpItem = "",
                mostExpDay = "",
                dayCartList = converters.fromDayTableList(dayList)
            ))
            _groceryEntityList.value = entityList
        }
    }

    fun removeDayCart(date:String){
            viewModelScope.launch(Dispatchers.IO) {

                val mIterator = dayList.iterator()
                while(mIterator.hasNext()){
                    val m = mIterator.next()
                    if(m.dId == date){
                        mIterator.remove()
                    }
                }
                repository.updateMonthlyTable(MonthlyTable(
                    mId = CommonUtils.getMonthYearFromDate(date),
                    mostBought = "",
                    mostExpItem = "",
                    mostExpDay = "",
                    dayCartList = converters.fromDayTableList(dayList)
                ))
            }
    }

    private fun getTotalPurchased():Double{
        var totalPurchased = 0.0
        entityList.forEach {
            if(it.isPurChanged){
                totalPurchased += it.cash.toDouble()
            }
        }
        return totalPurchased
    }

}