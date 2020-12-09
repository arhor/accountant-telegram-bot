package org.arhor.photospot.accountanttelegrambot.service

interface SheetService {

    fun append(range: String, value: Any, vararg other: Any)
}