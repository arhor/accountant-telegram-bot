package org.arhor.photospot.accountanttelegrambot.command

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.lang.invoke.MethodHandles

class StartCommand : BotCommand(COMMAND_NAME, COMMAND_DESC) {

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<String>) {
        try {
            sender.execute(
                SendMessage().apply {
                    chatId = chat.id.toString()
                    text = "You've just started conversation with the bot"
                }
            )
        } catch (ex: TelegramApiException) {
            log.error("Sending message failed", ex)
        }
    }

    companion object {
        const val COMMAND_NAME = "start"
        const val COMMAND_DESC = "Currently do nothing"

        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}