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

    var cartTotal = MutableStateFlow(0.0)
    var purchasedTotal = MutableStateFlow(0.0)
    var totalMonthlyExp = MutableStateFlow(0.0)
    var monthlyData = MutableStateFlow(MonthlyTable(
        mostBought = "", mostExpDay = "", mostExpItem = "", mId = "", dayCartList = ""
    ))

    private val converters = Converters()
    private val dayList = mutableListOf<DayTable>()
    private val entityList = mutableListOf<CartItemEntity>()

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
                cartItemList = entityList,
                totalExp = getTotalPurchased().first,
                mostExpItem = getMostExpItem()
            )
            dayList.add(updatedDayTable)
            repository.updateMonthlyTable(MonthlyTable(
                mId = CommonUtils.getMonthYearFromDate(date),
                mostBought = getMostBought(),
                mostExpItem = getMonthlyMostExpItem(),
                mostExpDay = getMostExpDay(),
                dayCartList = converters.fromDayTableList(dayList)
            ))
        }
    }

    fun getCartItems(mDate:String){
            viewModelScope.launch {
                val date1 = CommonUtils.getMonthYearFromDate(mDate)
                repository.getMonthlyCartItems(date1).collectLatest {
                        cartItems ->
                    dayList.clear()
                    entityList.clear()
                    if(cartItems != null){
                        dayList.addAll(converters.toDayTableList(cartItems.dayCartList))
                        dayList.forEach{
                            if(it.dId == mDate){
                                entityList.addAll(it.cartItemList)
                            }
                        }
                    }
                    withContext(Dispatchers.Main) {
                        _groceryEntityList.value = entityList.toList()
                        cartTotal.value = getTotalPurchased().second
                        purchasedTotal.value = getTotalPurchased().first
                        totalMonthlyExp.value = getTotalMonthlyExp()
                        monthlyData.value = cartItems
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

                entityList.add(
                    CartItemEntity(
                        id = model.id,
                        title = model.title,
                        cash = model.cash,
                        isPurChanged = model.isPurChanged,
                        quantity = model.quantity
                    ))
                val dayData = DayTable(
                    dId = date,
                    totalExp = getTotalPurchased().first,
                    cartItemList = entityList,
                    mostExpItem = getMostExpItem()
                )
                dayList.add(dayData)
                val data = MonthlyTable(
                    mId = date1,
                    mostBought = getMostBought(),
                    mostExpItem = getMonthlyMostExpItem(),
                    mostExpDay = getMostExpDay(),
                    dayCartList = converters.fromDayTableList(dayList)
                )
                repository.insertMonthlyTable(data)
            }

        }
    }


    fun removeCartItem(mId:Int,date: String){
        viewModelScope.launch {

            val updatedEntityList = entityList.filterNot { it.id == mId }.toMutableList()

            val updatedDayList = dayList.map { day ->
                if (day.dId == date) {
                    day.copy(cartItemList = updatedEntityList)
                } else {
                    day
                }
            }.toMutableList()

            repository.updateMonthlyTable(MonthlyTable(
                mId = CommonUtils.getMonthYearFromDate(date),
                mostBought = getMostBought(updatedDayList),
                mostExpItem = getMonthlyMostExpItem(updatedDayList),
                mostExpDay = getMostExpDay(updatedDayList),
                dayCartList = converters.fromDayTableList(updatedDayList)
            ))
        }
    }

    fun removeDayCart(date:String){
            viewModelScope.launch {

                val mIterator = dayList.iterator()
                while(mIterator.hasNext()){
                    val m = mIterator.next()
                    if(m.dId == date){
                        mIterator.remove()
                    }
                }
                repository.updateMonthlyTable(MonthlyTable(
                    mId = CommonUtils.getMonthYearFromDate(date),
                    mostBought = getMostBought(),
                    mostExpItem = getMonthlyMostExpItem(),
                    mostExpDay = getMostExpDay(),
                    dayCartList = converters.fromDayTableList(dayList)
                ))
            }
    }

     fun getTotalPurchased():Pair<Double,Double>{
        var totalPurchased = 0.0
        var totalCarted = 0.0
        entityList.forEach {
            if(it.isPurChanged){
                totalPurchased += it.cash.toDouble()
            }
            totalCarted += it.cash.toDouble()
        }
        return Pair(totalPurchased,totalCarted)
    }

    fun getTotalMonthlyExp(list:List<DayTable> = dayList):Double{
        var amount = 0.0
        list.forEach {
            amount += it.totalExp
        }
        return amount
    }

    private fun getMostExpItem():String{
        var amount = 0.0
        var i = 0
        entityList.forEachIndexed { index, item ->
            if(amount <= item.cash.toDouble()){
                amount = item.cash.toDouble()
                i = index
            }
        }
        return "${entityList[i].title}/${entityList[i].cash}"
    }

    private fun getMostExpDay(list:List<DayTable> = dayList):String{
        var high = 0.0
        var i = 0
        list.forEachIndexed { index, dayTable ->
            if(high <= dayTable.totalExp){
                high = dayTable.totalExp
                i = index
            }
        }
        return if(i!=0) list[i].dId else ""
    }

    private fun getMonthlyMostExpItem(list:List<DayTable> = dayList):String{
        var high = 0.0
        var itemName = ""
        list.forEach { dayTable ->
            val parts = dayTable.mostExpItem.split("/")
            if(high <= parts[1].toDouble()){
                high = parts[1].toDouble()
                itemName = parts[0]
            }
        }
        return itemName
    }

    private fun getMostBought(list:List<DayTable> = dayList):String{
        val memberList :MutableList<String> = mutableListOf()
        list.forEach {  dayTable ->
            dayTable.cartItemList.forEach {
                memberList.add(it.title)
            }
        }
        val stringCountMap = memberList.groupingBy { it }.eachCount()

        return stringCountMap.maxByOrNull { it.value }?.key?:""
    }

}