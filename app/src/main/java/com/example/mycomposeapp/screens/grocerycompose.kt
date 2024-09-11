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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mycomposeapp.utils.CommonUtils
import com.example.mycomposeapp.viewmodel.CartViewModel


@Composable
fun HomeScreenUi(viewModel: CartViewModel){

    var addNewItemDialog by remember { mutableStateOf(false) }
    var cartAnalysisDialog by remember { mutableStateOf(false) }
    var dateAlert by remember { mutableStateOf(false) }
    var todayDate by remember { mutableStateOf(CommonUtils.getTodayDate()) }

    viewModel.getCartItems(todayDate)

    Scaffold {
        ConstraintLayout (
            modifier = Modifier.fillMaxSize()
        ){
            val (appBar,list,button,bottomCard) = createRefs()
            AppBar(
                modifier = Modifier.constrainAs(appBar){
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
                modifier = Modifier.constrainAs(list){
                    top.linkTo(appBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(button.top, margin = 20.dp)
                    height = Dimension.fillToConstraints
                },
                viewModel
            )
            OutlinedButton(onClick = {
                addNewItemDialog = true
                                     },
                border = BorderStroke(2.dp,Color.Black),
                shape = RectangleShape,
                modifier = Modifier.constrainAs(button){
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end,margin = 10.dp)
                bottom.linkTo(bottomCard.top, margin = 15.dp)
                width = Dimension.fillToConstraints
            }
            ) {
                Text(text = "Add new item to Cart",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp)
            }
            BottomCard(modifier = Modifier.constrainAs(bottomCard){
                bottom.linkTo(parent.bottom, margin = 20.dp)
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end,margin = 10.dp)
            })
            if (addNewItemDialog){
                    AddNewCartItem(isEdit = false,viewModel= viewModel, date = todayDate){
                        addNewItemDialog = false
                    }
            }
            if (cartAnalysisDialog){
                    CartAnalysis{
                        cartAnalysisDialog = false
                    }
            }
            if(dateAlert){
                DateInputDialog(
                    true,{
                        dateAlert = false
                    },{
                        todayDate = it
                        viewModel.getCartItems(it)
                    }
                )
            }

        }
    }
}

@Composable
private fun BottomCard(modifier:Modifier){
             ConstraintLayout(modifier = modifier
                 .fillMaxWidth()
                 .background(color = Color.Gray)
                 .padding(10.dp)) {
                 val (purchased,pTotal,cart,cartTotal,divider) = createRefs()
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
                 Text(text = "1000Rs",
                     fontWeight = FontWeight.Bold,
                     fontSize = 12.sp,
                     color = Color.White ,
                     modifier = Modifier.constrainAs(pTotal){
                         end.linkTo(parent.end,margin = 10.dp)
                         bottom.linkTo(cartTotal.top,margin = 10.dp)
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
                         end.linkTo(cartTotal.end)
                         width = Dimension.fillToConstraints
                     })
                 Text(text = "17080Rs",
                     fontWeight = FontWeight.Bold,
                     fontSize = 12.sp,
                     color = Color.White ,
                     modifier = Modifier.constrainAs(cartTotal){
                         end.linkTo(parent.end,margin = 10.dp)
                         bottom.linkTo(parent.bottom)

                     })
             }
}


@Composable
private fun GroceryList(modifier: Modifier,viewModel: CartViewModel){
    val gList by  viewModel.groceryMList.collectAsState(initial = emptyList())
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
            GroceryListItem(item)
        }
    }
}

@Composable
private fun GroceryListItem(data:GroceryModel){
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceAround) {
        Checkbox(checked = data.isPurChanged, onCheckedChange = {
        })
            Text(text = data.title,
                modifier = Modifier.width(100.dp),
                fontWeight = FontWeight.SemiBold)
            Text(text = data.quantity,
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.W300)
            Text(text = data.cash,
                modifier = Modifier.width(60.dp),
                fontWeight = FontWeight.W300,
            )
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
        }
    }
}