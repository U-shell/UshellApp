package lanltn.com.circleprogresscustom.data

import androidx.compose.ui.graphics.Color
import ru.ushell.app.ui.screens.profileScreen.diagram.Level
import ru.ushell.app.ui.screens.profileScreen.diagram.Progress

object DataSources {

    val allLearnedProgresss = listOf(
        Progress(
            name = "Просмотренные\nлекций",
            allClasses = 20,
            finishedClasses = 0,
            level = Level.Easy,
            colorMain = Color(0xFF6650a4),
            progressColor = Color(0xFF6650a4),
            baseColor = Color(0xFFF2F4FF)
        ),
        Progress(
            name = "Посещаемость\nзанятий",
            allClasses = 30,
            finishedClasses = 0,
            level = Level.Medium,
            colorMain = Color(0xFFFFF6EB),
            isLightColor = true,
            progressColor = Color(0xFFF76400),
            baseColor = Color(0xFFFEEAD9)
        ),
        Progress(
            name = "Рейтинг\n ",
            allClasses = 20,
            finishedClasses = 0,
            level = Level.Hard,
            colorMain = Color(0xFF051774),
            progressColor = Color(0xFF2295F8),
            baseColor = Color(0xFFA4B0EE)
        ),
    )
}