//package ru.ushell.app.screens.profile.diagram
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun ProgressItem(course: Progress, modifier: Modifier) {
//    Surface(
//        color = _root_ide_package_.ru.ushell.app.old.ui.theme.DiagramBackgroundColor,
//        modifier = modifier
//            .clip(RoundedCornerShape(15.dp))
//    ) {
//        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .padding(
//                    start = 8.dp,
//                    end = 8.dp,
//                    top = 8.dp,
//                )
//        ) {
//            CircleProgressCustom(
//                allSteps = course.allClasses,
//                finishedSteps = course.finishedClasses,
//                modifier = Modifier
//                    .padding(5.dp)
//                ,
//            )
//            Box(
//                Modifier
//                    .padding(
//                        top=5.dp,
//                        bottom=7.dp
//                    )
//            ) {
//                Text(
//                    text = course.name,
//                    fontSize = 10.sp,
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        color = Color.Black,
//                        fontWeight = FontWeight.Medium
//                    ),
//                    lineHeight = 10.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
//    }
//}
