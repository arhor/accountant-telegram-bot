package org.arhor.photospot.accountanttelegrambot.service.impl

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.lang.invoke.MethodHandles

class PhotospotAccountantBot(
    commands: List<IBotCommand> = emptyList(),
    private val username: String,
    private val apitoken: String,
) : TelegramLongPollingCommandBot() {

    init {
        registerCommands(commands)
        registerDefaultAction(::handleUnknownCommand)
    }

    override fun getBotToken(): String = apitoken

    override fun getBotUsername(): String = username

    override fun processNonCommandUpdate(update: Update) {
        try {
            update.message?.let {
                log.debug("Received message from [{}]", it.from?.firstName ?: "anonymous")

                execute(
                    SendMessage().apply {
                        chatId = it.chatId.toString()
                        text = "Type /help to see available commands."
                    }
                )
            }
        } catch (ex: TelegramApiException) {
            log.error("Sending message failed", ex)
        }
    }

    private fun handleUnknownCommand(sender: AbsSender, message: Message) {
        sender.execute(
            SendMessage().apply {
                chatId = message.chatId.toString()
                text = "The command '${message.text}' is not recognized. Type /help to see available commands."
            }
        )
    }

    private fun registerCommands(commands: List<IBotCommand>) {
        val maxCommandLength = commands.map { it.commandIdentifier.length }.maxOrNull() ?: 0

        for (command in commands) {
            val result = register(command)
            val identifier = command.commandIdentifier
            val padding = " ".repeat(maxCommandLength - identifier.length) + "-"
            log.debug(
                "Registering command [{}] {} [{}]",
                identifier,
                padding,
                if (result) "SUCCESS" else "FAILURE"
            )
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
