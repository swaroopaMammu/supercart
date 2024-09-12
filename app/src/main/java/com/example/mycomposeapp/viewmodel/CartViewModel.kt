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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartViewModel (val repository: CartRepository) : ViewModel() {


    private val _groceryEntityList = MutableStateFlow<List<CartItemEntity>>(emptyList())
    val groceryEntityList: StateFlow<List<CartItemEntity>> = _groceryEntityList.asStateFlow()

    lateinit var monthData:MonthlyTable

    val converters = Converters()
    val mList = mutableListOf<DayTable>()
    val dList = mutableListOf<CartItemEntity>()

    fun updateCartItem(item: GroceryModel, date:String){
        viewModelScope.launch {
            val dIterator = dList.iterator()
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
            dList.add(entity)

            val mIterator = mList.iterator()
            while (mIterator.hasNext()) {
                val m = mIterator.next()
                if (m.dId == date) {
                    val updatedDayTable = DayTable(
                        dId = m.dId,
                        cartItemList = converters.fromCartItemList(dList),
                        totalExp = 0.0
                    )
                    mIterator.remove() // Safe removal
                    mList.add(updatedDayTable) // Add updated day table
                }
            }
            repository.updateMonthlyTable(MonthlyTable(
                mId = CommonUtils.getMonthYearFromDate(date),
                mostBought = monthData.mostBought,
                mostExpItem = monthData.mostExpItem,
                mostExpDay = monthData.mostExpDay,
                dayCartList = converters.fromDayTableList(mList)
            ))
        }
    }

    fun getCartItems(mDate:String){
            viewModelScope.launch {
                val date1 = CommonUtils.getMonthYearFromDate(mDate)
                repository.getMonthlyCartItems(date1).collectLatest {
                        cartItems ->
                    if(cartItems != null){
                        mList.clear()
                        dList.clear()
                        mList.addAll(converters.toDayTableList(cartItems.dayCartList))
                        mList.forEach{
                            if(it.dId == mDate){
                                dList.addAll(converters.toCartItemList(it.cartItemList))
                            }
                        }
                        monthData = cartItems
                        _groceryEntityList.value = dList
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
        viewModelScope.launch {

            val dIterator =dList.iterator()
            while(dIterator.hasNext()){
                val d = dIterator.next()
                if(d.id == mId){
                    dList.remove(d)
                }
            }
//          dayList.forEach { d ->
//              if(d.id == mId){
//                  dayList.remove(d)
//              }
//          }
            val mIterator = mList.iterator()
            while(mIterator.hasNext()){
                val m = mIterator.next()
                if(m.dId == date){
                    val copyM = DayTable(
                        dId = m.dId,
                        cartItemList = converters.fromCartItemList(dList),
                        totalExp = 0.0
                    )
                    mList.remove(m)
                    mList.add(copyM)
                }
            }
//            monthList.forEach { m ->
//              if(m.dId == date){
//                 val copyM = DayTable(
//                     dId = m.dId,
//                     cartItemList = converters.fromCartItemList(dayList),
//                     totalExp = 0.0
//                 )
//                  monthList.remove(m)
//                  monthList.add(copyM)
//              }
//
//          }
            repository.updateMonthlyTable(MonthlyTable(
                mId = CommonUtils.getMonthYearFromDate(date),
                mostBought = monthData.mostBought,
                mostExpItem = monthData.mostExpItem,
                mostExpDay = monthData.mostExpDay,
                dayCartList = converters.fromDayTableList(mList)
            ))
        }
    }

    fun removeDayCart(date:String){
        viewModelScope.launch {
            viewModelScope.launch {
//                monthList.forEach { m ->
//                    if(m.dId == date){
//                        monthList.remove(m)
//                    }
//
//                }
                val mIterator = mList.iterator()
                while(mIterator.hasNext()){
                    val m = mIterator.next()
                    if(m.dId == date){
                        mList.remove(m)
                    }
                }
                repository.updateMonthlyTable(MonthlyTable(
                    mId = CommonUtils.getMonthYearFromDate(date),
                    mostBought = monthData.mostBought,
                    mostExpItem = monthData.mostExpItem,
                    mostExpDay = monthData.mostExpDay,
                    dayCartList = converters.fromDayTableList(mList)
                ))
            }
        }
    }

}