package com.example.mycomposeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.mycomposeapp.utils.CommonUtils.validateDate
import com.example.mycomposeapp.utils.DateVisualTransformation

@Composable
fun DateInputDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
){
    var date  by remember { mutableStateOf(  "" ) }
    var error  by remember { mutableStateOf(  false ) }
    if(openDialog){
        AlertDialog(onDismissRequest = {
            onDismissRequest ()
        },
            confirmButton = {  },
            title = {
                Text("Enter the date")
            },
            text = {
                Column {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(value = date, onValueChange = {
                            date = it
                        }, label = { Text(text = "Date") },
                            placeholder = { Text("Enter the date in dd/mm/yyyy") },
                            isError =  error,
                        )
                    }
                    if(error){
                        Text(validateDate(date).second, color = Color.Red)
                    }
                    Button(onClick = {
                        if(validateDate(date).first){
                            onConfirm(date)
                            onDismissRequest ()
                        }else{
                            error = true
                        }
                    }, shape = RectangleShape) {
                        Text("DONE")
                    }
                }
            }
        )
    }
}