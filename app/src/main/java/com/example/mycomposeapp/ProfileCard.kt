package com.example.mycomposeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposeapp.model.ProfileDetail

@Composable
fun ProfileCard(data:ProfileDetail){

    Card(
        modifier = Modifier
            .height(200.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(  modifier = Modifier
            .fillMaxSize().background(color = Color.Gray)) {

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = data.picture),
                        contentDescription = "profilePicture",
                        modifier = Modifier
                            .width(86.dp)
                            .height(90.dp)
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = "Name : ${data.name}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                ) {
                    Text(
                        text = "Description : ${data.details}",
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Occupation : ${data.occupation}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }
    }


}

@Composable
fun ProfileCardListUI() {

    val profileList: ArrayList<ProfileDetail> = arrayListOf()

    profileList.add(
        ProfileDetail(
            name = "Pheobe Buffay",
            picture = R.drawable.pheobe,
            details = "Floppy and Beautiful",
            occupation = "Masseuse"
        )
    )
    profileList.add(
        ProfileDetail(
            name = "Monica Geller",
            picture = R.drawable.monica,
            details = "Bossy and Lovely",
            occupation = "Chef"
        )
    )
    profileList.add(
        ProfileDetail(
            name = "Rachel Green",
            picture = R.drawable.rachel,
            details = "Spoiled and Stylish",
            occupation = "Stylist"
        )
    )
    profileList.add(
        ProfileDetail(
            name = "Chandler Bing",
            picture = R.drawable.chandler,
            details = "Sarcastic and Wierd",
            occupation = "Data administrator"
        )
    )
    profileList.add(
        ProfileDetail(
            name = "Joey",
            picture = R.drawable.joey,
            details = "Funny and Cute",
            occupation = "Actor"
        )
    )
    profileList.add(
        ProfileDetail(
            name = "Ross",
            picture = R.drawable.ross,
            details = "Nerdy and Creepy",
            occupation = "Paleontologist"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(profileList) { card ->
            ProfileCard(data = card)
        }
    }
}