package com.example.lct2023.view.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lct2023.ui.theme.interFontFamily
import kotlinx.coroutines.delay


interface CustomListDropDownEntity {
    val name: String
}

fun String.toDropdownItem(): CustomListDropDownEntity {
    val s = this
    return object : CustomListDropDownEntity {
        override val name: String
            get() = s
    }
}

@Composable
fun DropdownListView(
    modifier: Modifier,
    itemModifier: Modifier = Modifier,
    items: List<CustomListDropDownEntity>,
    defaultText: String = "Выберите",
    onSelect: (CustomListDropDownEntity) -> String?
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember(items) {
        mutableStateOf(defaultText)
    }

    var startShaking by remember {
        mutableStateOf(false)
    }

    var needChangePadding by remember {
        mutableStateOf(false)
    }

    val leftPadding by animateDpAsState(
        if (needChangePadding) {
            8.dp
        } else {
            4.dp
        }
    )

    val rightPadding by animateDpAsState(
        if (needChangePadding) {
            0.dp
        } else {
            4.dp
        }
    )

    val bgColor: Color by animateColorAsState(if (startShaking) Color.Red else MaterialTheme.colors.onBackground)

    LaunchedEffect(key1 = startShaking) {
        if (startShaking) {
            needChangePadding = true
            delay(100)
            needChangePadding = false
            delay(100)
            needChangePadding = true
            delay(100)
            needChangePadding = false
        }
        startShaking = false
    }


    Box(
        modifier = modifier.padding(start = leftPadding, end = rightPadding)
    ) {
//        BorderTextIcon(modifier = modifier, value = selectedText) {
//            if (items.isNotEmpty()) {
//                expanded = true
//            } else {
//                startShaking = true
//            }
//        }
        BorderTextField(
            value = selectedText,
            onValueChange = {},
            textStyle = LocalTextStyle.current.copy(
                fontSize = 12.sp,
                color = bgColor,
                fontFamily = interFontFamily,
                textAlign = TextAlign.Start
            ),
            readOnly = true,
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {

                IconButton(onClick = {
                    if (items.isNotEmpty()) {
                        expanded = true
                    } else {
                        startShaking = true
                    }
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colors.primary.copy(
                    alpha = ContentAlpha.high
                ), focusedBorderColor = MaterialTheme.colors.primary.copy(
                    alpha = ContentAlpha.high
                )
            )
        )

        if (items.isNotEmpty()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = itemModifier
                    .padding(horizontal = 4.dp)
            ) {
                items.forEachIndexed { idx, item ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        selectedText = onSelect(item) ?: defaultText
                    }) {
                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            fontFamily = interFontFamily,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}




@Preview
@Composable
fun PreviewBorder(){
    var txt by remember {
        mutableStateOf("value")
    }
    BorderTextIcon(value = txt) {
        txt += "sa"
    }
}

@Composable
fun BorderTextIcon(
    modifier: Modifier = Modifier,
    value: String,
    onIconClick: () -> Unit
) {
    Row(
        modifier = modifier.border(
            2.dp,
            MaterialTheme.colors.primary.copy(alpha = ContentAlpha.medium),
            RoundedCornerShape(4.dp)
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = value, textAlign = TextAlign.Start, fontFamily = interFontFamily, modifier = Modifier.padding(horizontal = 8.dp).weight(8f))
        IconButton(onClick = onIconClick) {
            Icon(
                modifier = Modifier.weight(2f),
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "",
                tint = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
            )
        }
    }


}

@Composable
fun BorderTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled).value
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    @OptIn(ExperimentalMaterialApi::class)
    (BasicTextField(
        value = value,
        modifier = modifier
            .background(colors.backgroundColor(enabled).value, shape),
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp),
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled,
                        isError,
                        interactionSource,
                        colors,
                        shape
                    )
                }
            )
        }
    ))
}