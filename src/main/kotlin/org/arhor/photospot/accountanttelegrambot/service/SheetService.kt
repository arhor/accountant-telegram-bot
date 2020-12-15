package org.arhor.photospot.accountanttelegrambot.service

import org.arhor.photospot.accountanttelegrambot.core.ActionResult

interface SheetService {

    fun append(range: String, value: Any, vararg other: Any) : ActionResult<String>
}