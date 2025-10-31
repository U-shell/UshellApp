package ru.ushell.app.screens.messenger.dialog.panel


import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ushell.app.R
import ru.ushell.app.screens.messenger.dialog.button.RecordButton
import ru.ushell.app.screens.messenger.dialog.button.RecordingIndicator
import ru.ushell.app.screens.messenger.dialog.button.emoji.InputSelector
import ru.ushell.app.screens.messenger.dialog.button.emoji.SelectorExpanded

@Composable
fun InputPanel(
    onMessageSent: (String) -> Unit,
    resetScroll: () -> Unit = {}
){
    InputPanelContext(
        onMessageSent = onMessageSent,
        resetScroll = resetScroll
    )
}

@Composable
fun InputPanelContext(
    onMessageSent: (String) -> Unit,
    resetScroll: () -> Unit = {},
){

    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    var textFieldFocusState by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var swipeOffset by remember { mutableFloatStateOf(0f) }


    val dismissKeyboard = { currentInputSelector = InputSelector.NONE }
    if (currentInputSelector != InputSelector.NONE) {
        BackHandler(onBack = dismissKeyboard)
    }


    Column(
        modifier = Modifier
            .background(Color(0xFFD7D7D7))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Bottom)
            ) {
                IconButton(onClick = { currentInputSelector = InputSelector.EMOJI }) {
                    Icon(
                        painterResource(id = R.drawable.messamger_emoji),
                        contentDescription = null
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                InputText(
                    textFieldValue = textState,
                    onTextChanged = { textState = it },
                    keyboardShown = currentInputSelector == InputSelector.NONE && textFieldFocusState,
                    onTextFieldFocused = { focused ->
                        if (focused) {
                            currentInputSelector = InputSelector.NONE
                            resetScroll()
                        }
                        textFieldFocusState = focused
                    },
                    onMessageSent = {
                        onMessageSent(textState.text)
                        textState = TextFieldValue()
                        resetScroll()
                    },
                    focusState = textFieldFocusState,
                    isRecording = isRecording,
                    swipeOffset = swipeOffset
                )
            }
            Box(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(Alignment.Bottom)
            ) {
                if (textState.text.isNotBlank()) {
                    IconSend(
                        sendMessageEnabled = true,
                        onMessageSent = {
                            onMessageSent(textState.text)
                            textState = TextFieldValue()
                            resetScroll()
                        }
                    )

                } else {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                    ){
                        RecordButton(
                            recording = isRecording,
                            swipeOffset = { swipeOffset },
                            onSwipeOffsetChange = { swipeOffset = it },
                            onStartRecording = {
                                if (!isRecording) {
                                    isRecording = true
                                    true
                                } else false
                            },
                            onFinishRecording = {
                                isRecording = false
                                onMessageSent("[Аудиосообщение]")
                                resetScroll()
                            },
                            onCancelRecording = {
                                isRecording = false
                            }
                        )

                    }
                }
            }
        }
        SelectorExpanded(
            onTextAdded = { textState = textState.addText(it) },
            currentSelector = currentInputSelector,
        )
    }
}


@Composable
fun InputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    keyboardShown: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    onMessageSent: (String) -> Unit,
    focusState: Boolean,
    isRecording: Boolean,
    swipeOffset: Float
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        AnimatedContent(
            targetState = isRecording,
            label = "text-field",
            modifier = Modifier
                .weight(1f)
        ) { recording ->
            Box {
                if (recording) {
                    RecordingIndicator { swipeOffset }
                } else {
                    UserInputTextField(
                        textFieldValue,
                        onTextChanged,
                        onTextFieldFocused,
                        keyboardType,
                        focusState,
                        onMessageSent,
                        Modifier.semantics {
                            keyboardShownProperty = keyboardShown
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun IconSend(
    sendMessageEnabled: Boolean,
    onMessageSent: () -> Unit,
) {
    IconButton(
        enabled = sendMessageEnabled,
        onClick = onMessageSent,
    ) {
        Icon(
            painterResource(id = R.drawable.message_icon_send),
            contentDescription =null,
            tint = Color.Black,
            modifier = Modifier
                .size(25.dp)
        )
    }
}




val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

@Composable
private fun BoxScope.UserInputTextField(
    textFieldValue: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    onTextFieldFocused: (Boolean) -> Unit,
    keyboardType: KeyboardType,
    focusState: Boolean,
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var lastFocusState by remember { mutableStateOf(false) }
    BasicTextField(
        value = textFieldValue,
        onValueChange = { onTextChanged(it) },
        modifier = modifier
            .fillMaxWidth()

            .align(Alignment.CenterStart)
            .onFocusChanged { state ->
                if (lastFocusState != state.isFocused) {
                    onTextFieldFocused(state.isFocused)
                }
                lastFocusState = state.isFocused
            },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions {
            if (textFieldValue.text.isNotBlank())
                onMessageSent(textFieldValue.text)
        },
        cursorBrush = SolidColor(LocalContentColor.current),
        textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
    )

    val disableContentColor =
        MaterialTheme.colorScheme.onSurfaceVariant
    if (textFieldValue.text.isEmpty() && !focusState) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
            ,
            text = stringResource(R.string.message),
            style = MaterialTheme.typography.bodyLarge.copy(color = disableContentColor)
        )
    }
}


private fun TextFieldValue.addText(newString: String): TextFieldValue {
    val newText = this.text.replaceRange(
        this.selection.start,
        this.selection.end,
        newString
    )
    val newSelection = TextRange(
        start = newText.length,
        end = newText.length
    )

    return this.copy(text = newText, selection = newSelection)
}


@Preview
@Composable
fun InputPanelPreview(){
    InputPanel(
        onMessageSent = {}
    )
}
