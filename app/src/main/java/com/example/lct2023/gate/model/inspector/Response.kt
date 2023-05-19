package com.example.lct2023.gate.model.inspector



@kotlinx.serialization.Serializable
data class InspectorWaitListResponse(
    val list: List<InspectorWaitListResponseItem>
)

@kotlinx.serialization.Serializable
data class InspectorWaitListResponseItem(
    val id: String,
    val sCaption: String,
    val dDate: Long
)