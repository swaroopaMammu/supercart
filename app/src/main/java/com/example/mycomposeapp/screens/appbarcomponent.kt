package com.example.mycomposeapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposeapp.R
import com.example.mycomposeapp.utils.CommonUtils.formattingDate
import com.example.mycomposeapp.ui.theme.IconWhite
import com.example.mycomposeapp.ui.theme.AppBarBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(modifier: Modifier, cartClick:()->Unit, dateClick:()->Unit, date:String){
    TopAppBar( modifier = modifier,
        title = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = stringResource(id = R.string.super_cart),
                    fontWeight = FontWeight.Bold,
                    color = IconWhite,
                    fontSize = 26.sp,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBarBlue),
        actions = {
            IconButton(onClick = { cartClick() }) {
                Icon(imageVector = Icons.Rounded.ShoppingCart, contentDescription = stringResource(id = R.string.shopping_cart), tint = IconWhite)
            }
            IconButton(onClick = { dateClick() }) {
                Icon(imageVector = Icons.Rounded.DateRange, contentDescription = stringResource(id = R.string.date_filter), tint = IconWhite)
            }
        },
        navigationIcon = {
            val result = formattingDate(date)
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = result.first,
                    fontWeight = FontWeight.Bold,
                    color = IconWhite,
                    fontSize = 12.sp)
                Text(text = result.second,
                    fontWeight = FontWeight.Bold,
                    color = IconWhite,
                    fontSize = 16.sp)
            }
        }
    )
}