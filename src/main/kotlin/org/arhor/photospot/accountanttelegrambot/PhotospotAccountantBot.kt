package org.arhor.photospot.accountanttelegrambot;

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import java.lang.invoke.MethodHandles
import java.util.concurrent.CompletableFuture


class PhotospotAccountantBot(
    private val username: String,
    private val apitoken: String,
    commands: List<IBotCommand>
) : TelegramLongPollingCommandBot() {

    init {
        val maxCommandLength = commands.map { it.commandIdentifier.length }.maxOrNull() ?: 0

        commands.forEach {
            val result = if (register(it)) "SUCCESS" else "FAILURE"
            val padding = " ".repeat(maxCommandLength - it.commandIdentifier.length) + "-"
            log.debug("Registering command [{}] {} [{}]", it.commandIdentifier, padding, result)
        }

        registerDefaultAction(::handleUnknownCommand)
    }

    override fun getBotToken(): String = apitoken

    override fun getBotUsername(): String = username

    override fun processNonCommandUpdate(update: Update) {
        update.message?.let { message ->
            echo(message)
        }
    }

    private fun echo(message: Message): CompletableFuture<Message> {
        log.debug("Received message from [{}]", message.from?.firstName ?: "anonymous")

        return executeAsync(
            SendMessage().apply {
                chatId = message.chatId.toString()
                text = message.text
            }
        ).exceptionally(logExecutionError("Sending message failed"))
    }

    private fun handleUnknownCommand(sender: AbsSender, message: Message) {
        sender.executeAsync(
            SendMessage().apply {
                chatId = message.chatId.toString()
                text = "The command '${message.text}' is not known by this bot."

            }
        ).exceptionally(logExecutionError("Sending message failed"))
    }

    private fun <T> logExecutionError(msg: String): (Throwable) -> T? {
        return { throwable ->
            log.error(msg, throwable)
            null
        }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
