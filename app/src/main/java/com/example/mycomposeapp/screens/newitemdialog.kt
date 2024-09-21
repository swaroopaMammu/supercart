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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposeapp.model.GroceryModel
import com.example.mycomposeapp.utils.CommonUtils
import com.example.mycomposeapp.viewmodel.CartViewModel
import com.example.mycomposeapp.R
import com.example.mycomposeapp.utils.AppConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewCartItem(date:String,isEdit:Boolean,model: GroceryModel = GroceryModel(),viewModel: CartViewModel,onDismiss:()->Unit){

    val quality =  CommonUtils.splitBySeparator(model.quantity,AppConstants.SPACE)
    var itemName  by remember { mutableStateOf( if (isEdit) model.title else AppConstants.EMPTY ) }
    var quantity by remember { mutableStateOf(if (isEdit) quality.first else AppConstants.EMPTY) }
    var units by remember { mutableStateOf(if (isEdit) quality.second else AppConstants.EMPTY) }
    var price by remember { mutableStateOf( if(isEdit) model.cash else AppConstants.EMPTY) }
    var error by remember { mutableStateOf( false) }
    var headline :String
    var buttonText :String

    ModalBottomSheet(
        onDismissRequest = { onDismiss() }) {

        Column(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(isEdit){
                 headline = stringResource(id = R.string.edit_grocery_item)
                buttonText = stringResource(id = R.string.update_the_row)
            }else{
                 headline = stringResource(id = R.string.new_grocery_item)
                buttonText = stringResource(id = R.string.add_to_cart)
            }
            Text(text = headline, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            OutlinedTextField(
                value = itemName,
                onValueChange = {
                    itemName = it
                },
                label = { Text(text = stringResource(id = R.string.item_name)) },
                placeholder = {
                    Text(text = stringResource(id = R.string.enter_the_item_name))
                },
                modifier = Modifier.fillMaxWidth(),
                isError = itemName.isEmpty() && error
            )
            Row {
                OutlinedTextField(value = quantity,
                    onValueChange = { newValue ->
                        if (CommonUtils.isDouble(newValue)) {
                            quantity = newValue
                        }
                    },
                    label = { Text(text = stringResource(id = R.string.quantity)) },
                    placeholder = {
                        Text(text = stringResource(id = R.string.enter_the_quantity))
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = validateError(quantity,error)
                )
                Spacer(modifier = Modifier.width(20.dp))
                OutlinedTextField(
                    value = units,
                    onValueChange = {
                        units = it
                    },
                    label = { Text(text = stringResource(id = R.string.unit)) },
                    placeholder = {
                        Text(text = stringResource(id = R.string.unit) )
                    },
                    visualTransformation = VisualTransformation.None,
                    isError =  validateError(units,error)
                )
            }
            OutlinedTextField(
                value = price,
                onValueChange = { newValue ->
                    if (CommonUtils.isDouble(newValue)) {
                        price = newValue
                    }
                },
                label = { Text(text = stringResource(id = R.string.price)) },
                placeholder = {
                    Text(text = stringResource(id = R.string.enter_the_price))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number  // Shows numeric keyboard
                ),
                visualTransformation = VisualTransformation.None,
                isError = validateError(price,error)
            )
            if(error){
                Text(text = stringResource(id = R.string.no_field_can_be_left), color = Color.Red)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = {
                    if(validateButton(price, units, itemName, quantity)){
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
                        }else{
                            viewModel.updateMonthlyCartItems(date, GroceryModel(
                                id = kotlin.random.Random.nextInt(),
                                isPurChanged = false,
                                title = itemName,
                                quantity = "$quantity $units",
                                cash = price,
                            ))
                        }
                        onDismiss()
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
              Text(text = buttonText, color = Color.White)
            }
        }
    }
}

private fun validateError(quantity:String,error: Boolean):Boolean{
   return quantity.isEmpty() && error
}

private fun validateButton(price:String,units:String,itemName:String,quantity:String):Boolean{
   return price.isEmpty() || units.isEmpty() || itemName.isEmpty() || quantity.isEmpty()
}
