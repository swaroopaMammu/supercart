package com.example.mycomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapp.model.GroceryModel
import com.example.mycomposeapp.model.CartItemEntity
import com.example.mycomposeapp.model.DayTable
import com.example.mycomposeapp.model.db.entity.MonthlyTable
import com.example.mycomposeapp.model.repository.CartRepository
import com.example.mycomposeapp.utils.AppConstants
import com.example.mycomposeapp.utils.CommonUtils
import com.example.mycomposeapp.utils.Converters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel (private val repository: CartRepository) : ViewModel() {


    private val _groceryEntityList = MutableStateFlow<List<CartItemEntity>>(emptyList())
    val groceryEntityList: StateFlow<List<CartItemEntity>> = _groceryEntityList.asStateFlow()

    private val _cartTotal = MutableStateFlow(0.0)
    val cartTotal: StateFlow<Double> = _cartTotal.asStateFlow()

    private val _purchasedTotal = MutableStateFlow(0.0)
    val purchasedTotal: StateFlow<Double> = _purchasedTotal.asStateFlow()


    var monthlyData : MonthlyTable? = MonthlyTable()

    private val converters = Converters()
    private val dayList = mutableListOf<DayTable>()
    private val entityList = mutableListOf<CartItemEntity>()

    lateinit var todayDate :String


    fun updateCartItem(item: GroceryModel, date:String){
        viewModelScope.launch {
            val entity = CartItemEntity(
                id = item.id,
                cash = item.cash,
                isPurChanged = item.isPurChanged,
                quantity = item.quantity,
                title = item.title
            )
            val clonedEntityList = entityList.toMutableList()
            val index = clonedEntityList.indexOfFirst { it.id == item.id }
            if (index != -1) {
                clonedEntityList[index] = entity
            } else {
                clonedEntityList.add(entity)
            }

            val updatedDayTable = DayTable(
                dId = date,
                cartItemList = clonedEntityList,
                totalExp = getTotalPurchased(clonedEntityList).first,
                mostExpItem = getMostExpItem(clonedEntityList)
            )
            val clonedDayList = dayList.toMutableList()
            val index1 = clonedDayList.indexOfFirst { it.dId == date }
            if (index1 != -1) {
                clonedDayList[index1] = updatedDayTable
            } else {
                clonedDayList.add(updatedDayTable)
            }

            repository.updateMonthlyTable(
                MonthlyTable(
                mId = CommonUtils.getMonthYearFromDate(date),
                mostBought = getMostBought(clonedDayList),
                mostExpItem = getMonthlyMostExpItem(clonedDayList),
                mostExpDay = getMostExpDay(clonedDayList),
                dayCartList = converters.fromDayTableList(clonedDayList),
                totalExpense = getTotalMonthlyExp(clonedDayList)
            )
            )
        }
    }

    fun getCartItems(mDate:String){
        println("mDate in getCartItems out: $mDate")
        todayDate = mDate
            viewModelScope.launch {
                val date1 = CommonUtils.getMonthYearFromDate(mDate)
                repository.getMonthlyCartItems(date1).collectLatest {
                        cartItems ->
                    dayList.clear()
                    entityList.clear()
                    println("mDate in getCartItems in: $todayDate")
                    println("mDate in getCartItems mDate: $mDate")
                    cartItems?.let {
                        dayList.addAll(converters.toDayTableList(cartItems.dayCartList))
                        dayList.find { it.dId == todayDate }?.let { day ->
                            entityList.addAll(day.cartItemList)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        val total = getTotalPurchased()
                        _groceryEntityList.value = entityList.toList()
                        _cartTotal.value = total.second
                        _purchasedTotal.value = total.first
                        monthlyData = cartItems
                    }
                }
            }
    }

    fun updateMonthlyCartItems(date:String,model:GroceryModel) { // add new item
        val date1 = CommonUtils.getMonthYearFromDate(date)
        viewModelScope.launch {
            val count = repository.isCartItemExists(date1)
            if(count != AppConstants.DEFAULT_INT){
                updateCartItem(item = model,date=date)
            }else{

                entityList.add(
                    CartItemEntity(
                        id = model.id,
                        title = model.title,
                        cash = model.cash,
                        isPurChanged = model.isPurChanged,
                        quantity = model.quantity
                    )
                )
                val dayData = DayTable(
                    dId = date,
                    totalExp = getTotalPurchased().first,
                    cartItemList = entityList,
                    mostExpItem = getMostExpItem(entityList)
                )
                dayList.add(dayData)
                val data = MonthlyTable(
                    mId = date1,
                    mostBought = getMostBought(),
                    mostExpItem = getMonthlyMostExpItem(),
                    mostExpDay = getMostExpDay(),
                    dayCartList = converters.fromDayTableList(dayList),
                    totalExpense = getTotalMonthlyExp()
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
                    day.copy(cartItemList = updatedEntityList,
                        totalExp = getTotalPurchased(updatedEntityList).first,
                        mostExpItem = getMostExpItem(updatedEntityList))
                } else {
                    day
                }
            }.toMutableList()

            repository.updateMonthlyTable(
                MonthlyTable(
                mId = CommonUtils.getMonthYearFromDate(date),
                mostBought = getMostBought(updatedDayList),
                mostExpItem = getMonthlyMostExpItem(updatedDayList),
                mostExpDay = getMostExpDay(updatedDayList),
                dayCartList = converters.fromDayTableList(updatedDayList),
                totalExpense = getTotalMonthlyExp(updatedDayList)
            )
            )
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
                repository.updateMonthlyTable(
                    MonthlyTable(
                    mId = CommonUtils.getMonthYearFromDate(date),
                    mostBought = getMostBought(),
                    mostExpItem = getMonthlyMostExpItem(),
                    mostExpDay = getMostExpDay(),
                    dayCartList = converters.fromDayTableList(dayList),
                    totalExpense = getTotalMonthlyExp()
                )
                )
            }
    }

     private fun getTotalPurchased(list:List<CartItemEntity> = entityList):Pair<Double,Double>{
        var totalPurchased = AppConstants.DEFAULT_DOUBLE
        var totalCarted = AppConstants.DEFAULT_DOUBLE
         list.forEach {
            if(it.isPurChanged){
                totalPurchased += it.cash.toDouble()
            }
            totalCarted += it.cash.toDouble()
        }
        return Pair(totalPurchased,totalCarted)
    }

    private fun getTotalMonthlyExp(list:List<DayTable> = dayList):Double{
        var amount = AppConstants.DEFAULT_DOUBLE
        list.forEach {
            amount += it.totalExp
        }
        return amount
    }

    private fun getMostExpItem(entityList:List<CartItemEntity>):String{
        var amount = AppConstants.DEFAULT_DOUBLE
        var i = AppConstants.DEFAULT_INT
        entityList.forEachIndexed { index, item ->
            if(amount <= item.cash.toDouble()){
                amount = item.cash.toDouble()
                i = index
            }
        }
        return if(entityList.isNotEmpty()) "${entityList[i].title}${AppConstants.SLASH}${entityList[i].cash}" else AppConstants.EMPTY
    }

    private fun getMostExpDay(list:List<DayTable> = dayList):String{
        var high = AppConstants.DEFAULT_DOUBLE
        var i = AppConstants.DEFAULT_INT
        list.forEachIndexed { index, dayTable ->
            if(high <= dayTable.totalExp){
                high = dayTable.totalExp
                i = index
            }
        }
        return if(list.isNotEmpty()) list[i].dId else AppConstants.EMPTY
    }

    private fun getMonthlyMostExpItem(list:List<DayTable> = dayList):String{
        var high = AppConstants.DEFAULT_DOUBLE
        var itemName = AppConstants.EMPTY
        val firstPos = 0
        val secondPos = 1
        list.forEach { dayTable ->
            val parts = dayTable.mostExpItem.split(AppConstants.SLASH)
            if(parts.first().isNotEmpty() && (high <= parts[secondPos].toDouble())){
                high = parts[secondPos].toDouble()
                itemName = parts[firstPos]
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
        return stringCountMap.maxByOrNull { it.value }?.key?:AppConstants.EMPTY
    }

}