package com.example.lct2023.view.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
interface SearchingEntity {
    fun filter(query: String): Boolean
}

@Stable
interface ValueAutoCompleteEntity<T> : SearchingEntity {
    val value: T
}

private typealias ItemSelected<T> = (T) -> Unit

@Stable
interface AutoCompleteScope<T : SearchingEntity> : AutoCompleteDesignScope {
    var isSearching: Boolean
    fun filter(query: String)
    fun onItemSelected(block: ItemSelected<T> = {})
}





@Stable
interface AutoCompleteDesignScope {
    var boxWidthPercentage: Float
    var shouldWrapContentHeight: Boolean
    var boxMaxHeight: Dp
    var boxBorderStroke: BorderStroke
    var boxShape: Shape
}




class AutoCompleteState<T : SearchingEntity>(private val startItems: List<T>) : AutoCompleteScope<T> {
    private var onItemSelectedBlock: ItemSelected<T>? = null

    fun selectItem(item: T) {
        onItemSelectedBlock?.invoke(item)
    }

    var filteredItems by mutableStateOf(startItems)
    override var isSearching by mutableStateOf(false)
    override var boxWidthPercentage by mutableStateOf(.9f)
    override var shouldWrapContentHeight by mutableStateOf(false)
    override var boxMaxHeight: Dp by mutableStateOf(TextFieldDefaults.MinHeight * 3)
    override var boxBorderStroke by mutableStateOf(BorderStroke(2.dp, Color.Black))
    override var boxShape: Shape by mutableStateOf(RoundedCornerShape(8.dp))

    override fun filter(query: String) {
        if (isSearching)
            filteredItems = startItems.filter { entity ->
                entity.filter(query)
            }
    }

    override fun onItemSelected(block: ItemSelected<T>) {
        onItemSelectedBlock = block
    }
}

typealias CustomFilter<T> = (T, String) -> Boolean

@Composable
fun <T> List<T>.asSearchingEntities(filter: CustomFilter<T>): List<ValueAutoCompleteEntity<T>> {
    return map {
        object : ValueAutoCompleteEntity<T> {
            override val value: T = it

            override fun filter(query: String): Boolean {
                return filter(value, query)
            }
        }
    }
}


@ExperimentalAnimationApi
@Composable
fun <T : SearchingEntity> AutoCompleteBox(
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    content: @Composable AutoCompleteScope<T>.() -> Unit
) {
    val autoCompleteState = remember { AutoCompleteState(startItems = items) }



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        autoCompleteState.content()
        AnimatedVisibility(visible = autoCompleteState.isSearching) {
            Column(
                modifier = Modifier
                    .autoComplete(autoCompleteState)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                autoCompleteState.filteredItems.forEach { item ->
                    Box(modifier = Modifier.clickable { autoCompleteState.selectItem(item) }) {
                        itemContent(item)
                    }
                }
            }
        }
    }
}

private fun Modifier.autoComplete(
    autoCompleteItemScope: AutoCompleteDesignScope
): Modifier {

    val baseModifier = if (autoCompleteItemScope.shouldWrapContentHeight)
        wrapContentHeight()
    else
        heightIn(0.dp, autoCompleteItemScope.boxMaxHeight)

    return baseModifier
//        .testTag(AutoCompleteBoxTag)
        .fillMaxWidth(autoCompleteItemScope.boxWidthPercentage)
        .border(
            border = autoCompleteItemScope.boxBorderStroke,
            shape = autoCompleteItemScope.boxShape
        )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchBoxView(
    modifier: Modifier = Modifier,
    list: List<CustomListDropDownEntity>,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent
    ),
    labelText: String = "Вводите...",
    defaultText: String = "",
    onFocusChanged: (Boolean) -> Unit = {},
    onClearClick: () -> Unit,
    onSelect: (CustomListDropDownEntity) -> Unit
) {

    val rl = remember {
        mutableStateListOf(*list.toTypedArray())
    }

    val searchingEntities = rl.asSearchingEntities(
        filter = { item, query ->
            item.name.lowercase().contains(query.lowercase())
        }
    )
    val focusManager = LocalFocusManager.current


    AutoCompleteBox(items = searchingEntities, itemContent = { or ->
        Row {
            Text(
                modifier = modifier.padding(horizontal = 6.dp),
                text = or.value.name
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
    }) {
        var value by remember { mutableStateOf(defaultText) }

        onItemSelected { item ->
            value = item.value.name
            onSelect(item.value)
            filter(value)
            focusManager.clearFocus()
        }


        TextSearchBar(
            value = value,
            label = labelText,
            onDoneActionClick = {
                focusManager.clearFocus()
            },
            onClearClick = {
                value = ""
                filter(value)
                onClearClick()
                focusManager.clearFocus()
            },
            onFocusChanged = { focusState ->
                isSearching = focusState && value.isNotEmpty()
                onFocusChanged(focusState)
            },
            onValueChanged = { query ->
                value = query
                isSearching = value.isNotEmpty()
                filter(value)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colors.primary.copy(
                    alpha = ContentAlpha.high
                ), focusedBorderColor = MaterialTheme.colors.primary.copy(
                    alpha = ContentAlpha.high
                )
            )
        )


    }
}

@Composable
fun TextSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    shape: Shape = MaterialTheme.shapes.small,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    onValueChanged: (String) -> Unit
) {

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChanged(it.isFocused) },
        value = value,
        onValueChange = { query ->
            onValueChanged(query)
        },
        label = { Text(text = label) },

        textStyle = MaterialTheme.typography.subtitle1,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { onClearClick() }) {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
            }
        },
        shape = shape,
        colors = colors,
        keyboardActions = KeyboardActions(onDone = { onDoneActionClick() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )
}