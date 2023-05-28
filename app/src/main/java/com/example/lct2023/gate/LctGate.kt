package com.example.lct2023.gate

import com.example.lct2023.gate.model.RegisterRequestBody
import com.example.lct2023.gate.model.UserResponseBody
import com.example.lct2023.gate.model.user.*
import com.example.lct2023.view.util.Slot
import com.example.lct2023.view.util.Time
import kotlinx.serialization.decodeFromString
import java.text.SimpleDateFormat
import java.util.*

class LctGate : Gate() {

    fun sendMsg() {
        makePostRequest("sendMsg", "")
    }

    fun getMe(): UserResponseBody {
        val result = makeGetRequest("users/me")
        return format.decodeFromString(result)
    }

    fun registerUser(body: RegisterRequestBody): UserResponseBody {
        val result = makePostRequest(
            "users/register",
            body
        )
        return format.decodeFromString(result)
    }

    fun getSlots(knoId: Int): List<Slot> {
        val answer = makeGetRequest("slots/opened_list", mapOf("supervisor_id" to "$knoId"))
        val slots = format.decodeFromString<SlotResponse>(answer).open_slots
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val mapResult = mutableMapOf<Date, MutableList<Time>>()
        slots.forEach {
            formatter.parse(it.slot_date)?.let { date ->
                if (mapResult[date] != null) {
                    mapResult[date]!!.add(Time(it.id, it.slot_time))
                } else {
                    mapResult[date] = mutableListOf(Time(it.id, it.slot_time))
                }
            }
        }
        val result = mapResult.map { (k,v) -> Slot(k, v) }
        return result
    }

    fun getSupervisors(): List<Kno> {
        val response = makeGetRequest("supervisors/list")
        val answer = format.decodeFromString<List<KnoResponse>>(response)

        return answer.map {
            Kno(
                it.id,
                it.name,
                it.visions.map { ct -> ControlType(ct.id, ct.name) })
        }
    }

    fun getSupervisorsClean(): List<KnoResponse> {
        val response = makeGetRequest("supervisors/list")
        val answer = format.decodeFromString<List<KnoResponse>>(response)

        return answer
    }

    fun getTopicList(): List<ConsultTheme> {
        val response = makeGetRequest("topics/list")
        val answer = format.decodeFromString<List<ConsultThemeResponse>>(response)
        return answer.map { ConsultTheme(it.id, it.name) }
    }

    fun reserveMeet(slotId: String, topicId: String){
        val response = makePostRequest("/meetings/reserve", "", mapOf("slot_id" to slotId, "topic_id" to topicId))
    }

    fun approveMeet(appointment_id: String){
        val response = makePatchRequest("meetings/approve/${appointment_id}","")
    }

    fun rejectMeet(meeting_id: String){
        val response = makePatchRequest("meetings/approve/${meeting_id}", "")
    }

    fun getApprovedList(): List<InspectorSlotResponse>{
        val response = makeGetRequest("slots/approved_list")
        return format.decodeFromString(response)
    }

}