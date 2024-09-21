package com.example.mycomposeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import com.example.mycomposeapp.utils.AppConstants
import com.example.mycomposeapp.utils.CommonUtils.validateDate
import com.example.mycomposeapp.viewmodel.CartViewModel
import com.example.mycomposeapp.R

@Composable
fun DateInputDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    viewModel: CartViewModel
){
    var date  by remember { mutableStateOf(  AppConstants.EMPTY ) }
    var error  by remember { mutableStateOf(  false ) }

    if(openDialog){
        AlertDialog(onDismissRequest = {
            onDismissRequest ()
        },
            confirmButton = {  },
            title = {
                Text(stringResource(id = R.string.enter_the_date))
            },
            text = {
                Column {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = date,
                            onValueChange = {
                                date = it
                            },
                            label = { Text(text = stringResource(id = R.string.date)) },
                            placeholder = { Text(stringResource(id = R.string.enter_the_date_in)) },
                            isError = error,
                        )
                    }
                    if(error){
                        Text(validateDate(date).second, color = Color.Red)
                    }
                    Button(onClick = {
                        if(validateDate(date).first){
                            viewModel.getCartItems(date)
                            onConfirm(date)
                            onDismissRequest ()
                        }else{
                            error = true
                        }
                    }, shape = RectangleShape,
                       colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Text(text = stringResource(id = R.string.done,), color = Color.White)
                    }
                }
            }
        )
    }
}