package com.example.mycomposeapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposeapp.model.GroceryModel
import com.example.mycomposeapp.model.db.entity.CartItemEntity
import com.example.mycomposeapp.utils.CommonUtils
import com.example.mycomposeapp.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewCartItem(isEdit:Boolean,model: GroceryModel = GroceryModel(
    id = 0, title = "", cash = "", quantity = "", isPurChanged = false,
),viewModel: CartViewModel,date:String,onDismiss:()->Unit){
    val quality = CommonUtils.splitNumericAndText(model.quantity)
    var itemName  by remember { mutableStateOf( if (isEdit) model.title else "" ) }
    var quantity by remember { mutableStateOf(if (isEdit) quality.first else "") }
    var units by remember { mutableStateOf(if (isEdit) quality.second else "") }
    var price by remember { mutableStateOf( if(isEdit) model.cash else "") }
    var error by remember { mutableStateOf( false) }
    ModalBottomSheet(onDismissRequest = {
        onDismiss()
    },
    ) {

        Column(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(isEdit)Text(text = "Edit grocery item", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            else Text(text = "New grocery item", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            OutlinedTextField(
                value = itemName,
                onValueChange = {
                    itemName = it
                },
                label = { Text(text = "Item name") },
                placeholder = {
                    Text(text = "Enter the item name")
                },
                modifier = Modifier.fillMaxWidth(),
                isError = itemName.isEmpty() && error
            )
            Row {
                OutlinedTextField(value = quantity,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            quantity = newValue
                        }

                    },
                    label = { Text(text = "Quantity") },
                    placeholder = {
                        Text(text = "Enter the quantity")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number  // Shows numeric keyboard
                    ),
                    isError = quantity.isEmpty() && error
                )
                Spacer(modifier = Modifier.width(20.dp))
                OutlinedTextField(
                    value = units,
                    onValueChange = {
                        units = it
                    },
                    label = { Text(text = "Units") },
                    placeholder = {
                        Text(text = " Units")
                    },
                    visualTransformation = VisualTransformation.None,
                    isError = units.isEmpty() && error
                )
            }
            OutlinedTextField(
                value = price,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        price = newValue
                    }
                },
                label = { Text(text = "Price") },
                placeholder = {
                    Text(text = "Enter the price in ₹")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number  // Shows numeric keyboard
                ),
                visualTransformation = VisualTransformation.None,
                isError = price.isEmpty() && error
            )
            if(error){
                Text(text = "no field can be left blank", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = {
                    if(price.isEmpty() || units.isEmpty() || itemName.isEmpty() || quantity.isEmpty()){
                        error = true
                    }else{
                        if(isEdit){
                            viewModel.updateCartItem(
                               item =  GroceryModel(
                                    id = model.id,
                                    isPurChanged = model.isPurChanged,
                                    title = itemName,
                                    quantity = "$quantity $units",
                                    cash = price
                                ),
                                date = date
                            )
                            onDismiss()
                        }else{
                            val item = GroceryModel(
                                id = kotlin.random.Random.nextInt(),
                                isPurChanged = true,
                                title = itemName,
                                quantity = "$quantity $units",
                                cash = price,
                            )
                            viewModel.updateMonthlyCartItems(date, item)

                            itemName = ""
                            quantity = ""
                            units = ""
                            price = ""
                            onDismiss()
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                shape = RectangleShape
            ) {
                if(isEdit) Text(text = "UPDATE THE ROW") else Text(text = "ADD TO CART")
            }
        }
    }
}
