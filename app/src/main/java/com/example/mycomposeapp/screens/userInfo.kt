package com.example.mycomposeapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.mycomposeapp.R

@Composable
fun UserInfoDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    title:String,
    description:String
){
    if(openDialog){
        AlertDialog(onDismissRequest = { onDismissRequest()},
            confirmButton = {  },
            title = {
                Text(text = title)
            },
            text = {
                Column {
                    Text(text = description)
                    Button(onClick = {
                        onDismissRequest()
                    }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Text(text = stringResource(id = R.string.ok_button), color = Color.White)
                    }
                }
            }
        )
    }
}