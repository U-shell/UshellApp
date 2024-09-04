package ru.ushell.app.ui.screens.profileScreen.diagram

import androidx.compose.ui.graphics.Color

data class Progress(
    val name: String,
    val allClasses: Int,
    val finishedClasses: Int,
    val level: Level,
    val colorMain: Color,
    val progressColor: Color = Color.Gray,
    val baseColor: Color = Color.Green,
    val isLightColor: Boolean = false,
)

enum class Level {
    Easy, Medium, Hard,
}