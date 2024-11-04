package ru.ushell.app.ui.screens.drawer.models.noise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun NoiseScreen(){
    NoiseContext()
}

@Composable
fun NoiseContext() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Noise",
                fontSize = 40.sp
            )
        }
    }
}

@Preview
@Composable
fun NoiseScreenPreview(){
    NoiseScreen()
}
