package com.example.lct2023.gate.model.user

import com.example.lct2023.view.util.CustomListDropDownEntity
import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class KnoResponse(
    val id: Int,
    val name: String,
    val visions: List<ControlTypeResponse>
)

@kotlinx.serialization.Serializable
data class Kno(
    val id: Int,
    override val name: String,
    val visions: List<CustomListDropDownEntity>
): CustomListDropDownEntity

@kotlinx.serialization.Serializable
data class ControlTypeResponse(
    val id: Int,
    val name: String
)

@kotlinx.serialization.Serializable
data class ControlType(
    val id: Int,
    override val name: String
): CustomListDropDownEntity

@kotlinx.serialization.Serializable
data class ConsultThemeResponse(
    val id: Int,
    val name: String
)

@kotlinx.serialization.Serializable
data class ConsultTheme(
    val id: Int,
    override val name: String
): CustomListDropDownEntity


//@kotlinx.serialization.Serializable
//data class SlotResponse(
//    List<OpenSlotResponse>
//)

@kotlinx.serialization.Serializable
data class OpenSlotResponse(
    val id: Int,
    val slot_date: String,
    val slot_time: String
)

@kotlinx.serialization.Serializable
data class InspectorSlotResponse(val id: Int, val date: String, val time: String)