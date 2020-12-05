package org.arhor.photospot.accountanttelegrambot.command

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import java.lang.invoke.MethodHandles

class StartCommand : BotCommand("start", "") {

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<String>) {
        sender.executeAsync(
            SendMessage().apply {
                chatId = chat.id.toString()
                text = "You've just started conversation with the bot"
            }
        ).exceptionally { throwable ->
            log.error("Sending message failed", throwable)
            null
        }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}