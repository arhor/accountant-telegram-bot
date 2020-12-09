package org.arhor.photospot.accountanttelegrambot.command

import org.arhor.photospot.accountanttelegrambot.service.SheetService
import org.arhor.photospot.accountanttelegrambot.util.head
import org.arhor.photospot.accountanttelegrambot.util.tail
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.lang.invoke.MethodHandles

class AddCommand(private val sheetService: SheetService) : BotCommand(COMMAND_NAME, COMMAND_DESC) {

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<String>) {
        val data = parse(arguments)

        val result = when {
            data.isNotEmpty() -> {
                sheetService.append(range = "A1", value = data.head, other = data.tail)
                "Record `${data[0]}...` was added to the table"
            }
            else -> "Columns must not be empty"
        }

        try {
            sender.execute(
                SendMessage().apply {
                    chatId = chat.id.toString()
                    text = result
                }
            )
        } catch (ex: TelegramApiException) {
            log.error("Sending message failed", ex)
        }
    }

    private fun parse(values: Array<String>): List<String> {
        return values.joinToString(separator = SPACE_HOLDER)
            .split("/")
            .map { it.replace(SPACE_HOLDER, SEPARATOR) }
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    companion object {
        const val COMMAND_NAME = "add"
        const val COMMAND_DESC = ""

        const val SPACE_HOLDER = "$$"
        const val SEPARATOR = " "

        @JvmStatic
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}