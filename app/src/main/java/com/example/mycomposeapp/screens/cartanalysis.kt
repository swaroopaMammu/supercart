package com.example.mycomposeapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposeapp.model.db.entity.MonthlyTable
import com.example.mycomposeapp.utils.CommonUtils
import com.example.mycomposeapp.viewmodel.CartViewModel
import com.example.mycomposeapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartAnalysis(viewModel: CartViewModel,onDismiss:()->Unit){
    ModalBottomSheet(onDismissRequest = {
        onDismiss()
    },
    ) {
       val data by viewModel.monthlyData.collectAsState(MonthlyTable() )

        Column (
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "${stringResource(id = R.string.cart_monthly_analysis_of)} ${CommonUtils.getMonthFromDate(data.mId)}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = stringResource(id = R.string.monthly_expenses),fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "${data.totalExpense} ${stringResource(id = R.string.rs)}",fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row{
                Text(text = stringResource(id = R.string.most_exp_day),fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = data.mostExpDay,fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row{
                Text(text = stringResource(id = R.string.most_exp_item),fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = data.mostExpItem,fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(text = stringResource(id = R.string.most_bought_item),fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = data.mostBought,fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }

}