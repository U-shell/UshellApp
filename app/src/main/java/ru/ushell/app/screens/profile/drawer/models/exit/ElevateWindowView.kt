package ru.ushell.app.screens.profile.drawer.models.exit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.ushell.app.R
import ru.ushell.app.domain.service.loadData.LoadDataService
import ru.ushell.app.ui.theme.LightBackgroundColor


@Composable
fun ElevateWindowDialog(
    scope: CoroutineScope,
    drawerState: DrawerState,
    showExit: MutableState<Boolean>,
    navController: NavHostController,
    onDismiss: () -> Unit,
){
    Dialog(onDismissRequest = onDismiss) {
        Surface(color = Color.Transparent) {
            ElevateWindowContext(
                scope=scope,
                showExit=showExit,
                drawerState=drawerState,
                navController=navController
            )
        }
    }
}

@Composable
fun ElevateWindowContext(
    showExit: MutableState<Boolean>,
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
) {
    val exit = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(
                color = LightBackgroundColor,
                shape = RoundedCornerShape(10.dp),
            )
            .fillMaxWidth()
            .padding(10.dp),
    ){
        Box{
            Text(
                text = "Уверен?",
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            TextButton(
                onClick = {
                    showExit.value = false
                }
            ){
                Text(text = stringResource(id = R.string.cancellation))
            }
            TextButton(onClick = {

                exit.value=true
                showExit.value = false
                scope.launch {drawerState.close()}

            }) {
                Text(text = stringResource(id = R.string.drawer_exit))
            }
        }
    }
    if(exit.value){
        Exit()
        exit.value=false
    }
}

@Composable
fun Exit(
    loadDataService: LoadDataService = hiltViewModel()
) {
    loadDataService.logout(LocalContext.current)
}

@Preview
@Composable
fun ElevateWindowPreview(){
    val showExit = remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ElevateWindowDialog(
        scope=scope,
        showExit=showExit,
        drawerState=drawerState,
        navController=navController,
        onDismiss = {
            showExit.value = false
        },
    )
}
