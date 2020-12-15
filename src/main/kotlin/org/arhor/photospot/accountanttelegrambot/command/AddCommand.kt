package org.arhor.photospot.accountanttelegrambot.command

import org.arhor.photospot.accountanttelegrambot.core.ActionResult.Companion.failure
import org.arhor.photospot.accountanttelegrambot.service.SheetService
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.lang.invoke.MethodHandles
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AddCommand(private val sheetService: SheetService) : BotCommand(COMMAND_NAME, COMMAND_DESC) {

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<String>) {
        val data = parse(arguments)

        val result = when {
            data.isNotEmpty() -> {
                sheetService.append(
                    range = "A1",
                    value = LocalDateTime.now(MINSK_TIME).format(FORMATTER),
                    other = data.toTypedArray()
                )
            }
            else -> failure("Columns must not be empty")
        }

        try {
            sender.execute(
                SendMessage().apply {
                    chatId = chat.id.toString()
                    text = result.toString()
                }
            )
        } catch (ex: TelegramApiException) {
            log.error("Sending message failed", ex)
        }
    }

    private fun parse(values: Array<String>): List<String> {
        return values.joinToString(separator = SPACE_HOLDER)
            .split(DELIMITER)
            .map { it.replace(SPACE_HOLDER, SPACE_SYMBOL) }
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    companion object {
        const val COMMAND_NAME = "add"
        const val COMMAND_DESC = "Use the following format: '/add contact, course, payment, total"

        const val DELIMITER = ","
        const val SPACE_HOLDER = "$$"
        const val SPACE_SYMBOL = " "

        private val MINSK_TIME = ZoneId.of("UTC+03:00:00")
        private val FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}