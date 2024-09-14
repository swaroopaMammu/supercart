package com.example.mycomposeapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.mycomposeapp.model.GroceryModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mycomposeapp.utils.CommonUtils
import com.example.mycomposeapp.viewmodel.CartViewModel


@Composable
fun HomeScreenUi(viewModel: CartViewModel) {

    var addNewItemDialog by remember { mutableStateOf(false) }
    var cartAnalysisDialog by remember { mutableStateOf(false) }
    var dateAlert by remember { mutableStateOf(false) }
    var todayDate by remember { mutableStateOf(CommonUtils.getTodayDate()) }
    var isEdit by remember { mutableStateOf(false) }
    var dataModel by remember { mutableStateOf(GroceryModel()) }
    viewModel.getCartItems(todayDate)
    Scaffold { padding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val (appBar, list, button,delete, bottomCard) = createRefs()
            AppBar(
                modifier = Modifier.constrainAs(appBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                {
                    cartAnalysisDialog = true
                },
                {
                    dateAlert = true
                },
                date = todayDate
            )
            GroceryList(
                modifier = Modifier.constrainAs(list) {
                    top.linkTo(appBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(button.top, margin = 20.dp)
                    height = Dimension.fillToConstraints
                },
                viewModel= viewModel,
                date = todayDate,
                edit = {
                    addNewItemDialog = true
                    dataModel = it
                    isEdit = true
                },
                delete = {
                    viewModel.removeCartItem(it,todayDate)
                }
            )
            OutlinedButton(onClick = {
                addNewItemDialog = true
                isEdit = false
            },
                border = BorderStroke(2.dp, Color.Black),
                shape = RectangleShape,
                modifier = Modifier.constrainAs(button) {
                    start.linkTo(parent.start, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                    bottom.linkTo(delete.top, margin = 5.dp)
                    width = Dimension.fillToConstraints
                },
            ) {
                Text(
                    text = "Add new item to Cart",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }

            OutlinedButton(onClick = {
                 viewModel.removeDayCart(todayDate)
            },
                border = BorderStroke(2.dp, Color.Black),
                shape = RectangleShape,
                modifier = Modifier.constrainAs(delete) {
                    start.linkTo(parent.start, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                    bottom.linkTo(bottomCard.top, margin = 10.dp)
                    width = Dimension.fillToConstraints
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(
                    text = "Empty today's cart",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
            BottomCard(modifier = Modifier.constrainAs(bottomCard) {
                bottom.linkTo(parent.bottom, margin = 20.dp)
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
            },viewModel = viewModel )
            if (addNewItemDialog) {
                viewModel.todayDate = todayDate
                AddNewCartItem(isEdit = isEdit,
                    viewModel = viewModel,
                    model = dataModel) {
                    addNewItemDialog = false
                }
            }
            if (cartAnalysisDialog) {
                CartAnalysis(viewModel){
                    cartAnalysisDialog = false
                }
            }
            if (dateAlert) {
                DateInputDialog(
                    true, {
                        dateAlert = false
                    }, {
                        todayDate = it
                    },viewModel
                )
            }

        }
    }
}

@Composable
private fun BottomCard(modifier:Modifier,viewModel: CartViewModel){

    val purchasedTotal by viewModel.purchasedTotal.collectAsState(initial = 0.0)
    val cartTotal by  viewModel.cartTotal.collectAsState(initial = 0.0)

             ConstraintLayout(modifier = modifier
                 .fillMaxWidth()
                 .background(color = Color.Gray)
                 .padding(10.dp)) {
                 val (purchased,pTotal,cart,cTotal,divider) = createRefs()
                 Text(text = "Purchased total",
                     fontWeight = FontWeight.Bold,
                     fontSize = 18.sp,
                     color = Color.White ,
                     modifier = Modifier.constrainAs(purchased){
                         start.linkTo(parent.start, margin = 10.dp)
                         bottom.linkTo(cart.top, margin = 10.dp)
                         end.linkTo(pTotal.end)
                         width = Dimension.fillToConstraints
                     })
                 Text(text = "$purchasedTotal",
                     fontWeight = FontWeight.Bold,
                     fontSize = 12.sp,
                     color = Color.White ,
                     modifier = Modifier.constrainAs(pTotal){
                         end.linkTo(parent.end,margin = 10.dp)
                         bottom.linkTo(cTotal.top,margin = 10.dp)
                     })
                 HorizontalDivider(
                     modifier = Modifier
                         .constrainAs(divider) {
                             start.linkTo(parent.start, margin = 10.dp)
                             end.linkTo(parent.end, margin = 10.dp)
                             bottom.linkTo(cart.top)
                             top.linkTo(purchased.bottom)
                         }
                         .fillMaxWidth()
                         .padding(horizontal = 5.dp),
                     thickness = 2.dp,
                     color = Color.White
                 )
                 Text(text = "Cart total",
                     fontWeight = FontWeight.Bold,
                     fontSize = 24.sp,
                     color = Color.White ,
                     modifier = Modifier.constrainAs(cart){
                         start.linkTo(parent.start,margin = 10.dp)
                         bottom.linkTo(parent.bottom)
                         end.linkTo(cTotal.end)
                         width = Dimension.fillToConstraints
                     })
                 Text(text = "$cartTotal",
                     fontWeight = FontWeight.Bold,
                     fontSize = 12.sp,
                     color = Color.White ,
                     modifier = Modifier.constrainAs(cTotal){
                         end.linkTo(parent.end,margin = 10.dp)
                         bottom.linkTo(parent.bottom)

                     })
             }
}


@Composable
private fun GroceryList(modifier: Modifier,viewModel: CartViewModel,date:String,edit:(model:GroceryModel)->Unit,delete:(id:Int)->Unit){
    val gList by  viewModel.groceryEntityList.collectAsState(initial = emptyList())
    val gMList : MutableList<GroceryModel> = mutableListOf()
    gList.forEach { m ->
        gMList.add(
            GroceryModel(
                id = m.id,
                title = m.title,
                isPurChanged = m.isPurChanged,
                cash = m.cash,
                quantity = m.quantity
            )
        )
    }

    LazyColumn(modifier = modifier
        .fillMaxWidth()
        .padding(top = 10.dp)) {
        items(gMList) { item ->
            GroceryListItem(item,{
                edit(item)
            },{
                delete(it.id)
            },{
                viewModel.updateCartItem(
                   item = GroceryModel(
                    id = item.id,
                    isPurChanged = it,
                    title = item.title,
                    quantity = item.quantity,
                    cash = item.cash
                    ),date= date)
            })
        }
    }
}

@Composable
private fun GroceryListItem(data:GroceryModel,oneEditClick : ()->Unit,
                            onDelete:(data:GroceryModel)->Unit,onCheckChanged:(flag:Boolean)->Unit){
    var handleCheck by remember { mutableStateOf(data.isPurChanged) }
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceAround) {
        Checkbox(checked = handleCheck, onCheckedChange = {
            handleCheck = it
            onCheckChanged(handleCheck)
        })
            Text(text = data.title,
                modifier = Modifier.width(100.dp),
                fontWeight = FontWeight.SemiBold)
            Text(text = data.quantity,
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.W300)
            Text(text = "${data.cash} ₹",
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.W300,
            )
        IconButton(onClick = {
            oneEditClick()
        }) {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = { onDelete(data)}) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
        }
    }
}