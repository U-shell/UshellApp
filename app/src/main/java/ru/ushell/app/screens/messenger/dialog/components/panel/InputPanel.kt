package ru.ushell.app.screens.messenger.dialog.components.panel

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.ushell.app.R
import ru.ushell.app.data.features.messenger.dto.MessageType
import ru.ushell.app.data.features.messenger.mappers.Message
import ru.ushell.app.screens.messenger.dialog.components.button.RecordButton
import ru.ushell.app.screens.messenger.dialog.components.button.RecordingIndicator
import ru.ushell.app.screens.messenger.dialog.components.button.emoji.InputSelector
import ru.ushell.app.screens.messenger.dialog.components.button.emoji.SelectorExpanded
import ru.ushell.app.screens.messenger.dialog.components.panel.preview.PreviewFile
import ru.ushell.app.screens.messenger.dialog.components.panel.preview.PreviewImage
import java.time.OffsetDateTime

@Composable
fun InputPanel(
    onMessageSent: (Message) -> Unit,
    resetScroll: () -> Unit = {}
){
    InputPanelContext(
        onMessageSent = onMessageSent,
        resetScroll = resetScroll
    )
}

@Composable
fun InputPanelContext(
    onMessageSent: (Message) -> Unit,
    resetScroll: () -> Unit = {},
){
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    var textFieldFocusState by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var swipeOffset by remember { mutableFloatStateOf(0f) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                selectedFileUri = uri
                resetScroll()
            }
        }
    )

    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    val dismissKeyboard = { currentInputSelector = InputSelector.NONE }
    if (currentInputSelector != InputSelector.NONE) {
        BackHandler(onBack = dismissKeyboard)
    }

    val sendMessage = {
        val hasText = textState.text.isNotBlank()
        val hasFile = selectedFileUri != null

        if (hasText || hasFile) {
            val type = if (hasFile) MessageType.FILE else MessageType.TEXT
            onMessageSent(Message(
                author = true,
                message = textState.text,
                type = type,
                timestamp = OffsetDateTime.now(),
                uri = selectedFileUri
            ))
            textState = TextFieldValue()
            selectedFileUri = null
            resetScroll()
        }
    }


    Column(
        modifier = Modifier
            .background(Color(0xFFD7D7D7)),
        verticalArrangement = Arrangement.Center
    ) {
        if (selectedFileUri != null) {
            FilePreview(
                fileUri = selectedFileUri!!,
                onCancel = {
                    selectedFileUri = null
                }
            )
        }
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
                        sendMessage()
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
                if (textState.text.isNotBlank() || selectedFileUri != null) {
                    IconSend(
                        sendMessageEnabled = true,
                        onMessageSent = {
                            sendMessage()
                            resetScroll()
                        }
                    )

                } else {
                    Row(
                        modifier = Modifier
                            .wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        IconSendFile(
                            onLaunchPicker = { mimeType -> filePicker.launch(mimeType) }
                        )

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
                                sendMessage()
                                isRecording = false
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
fun FilePreview(
    fileUri: Uri,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val mimeType = remember(fileUri) {
        runCatching { context.contentResolver.getType(fileUri) }.getOrNull()
    }

    val messageType = when {
        mimeType?.startsWith("image/") == true -> MessageType.IMAGE
        mimeType?.startsWith("application/") == true -> MessageType.FILE
        mimeType?.startsWith("video/") == true -> MessageType.VIDEO
        else -> MessageType.UNKNOWN
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when(messageType){
            MessageType.IMAGE -> {
                PreviewImage(
                    fileUri=fileUri,
                    onCancel = onCancel
                )
            }
            MessageType.FILE -> {
                PreviewFile(
                    fileUri=fileUri,
                    onCancel = onCancel
                )
            }
            else -> {}
        }
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

@Composable
fun IconSendFile(
    onLaunchPicker: (String) -> Unit // Функция, которая запускает лаунчер с MIME-типом
){
    IconButton(
        onClick = {
            // Вызываем переданную функцию с нужным MIME-типом
            // "image/*" для изображений
            // "video/*" для видео
            // "*/*" для любых файлов
            onLaunchPicker("*/*") // Пример: только изображения
        },
    ) {
        Icon(
            painterResource(id = R.drawable.message_icon_send_file),
            contentDescription =null,
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
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
        onMessageSent = {},
    )
}
