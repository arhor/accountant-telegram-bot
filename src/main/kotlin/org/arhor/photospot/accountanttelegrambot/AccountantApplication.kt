package org.arhor.photospot.accountanttelegrambot

import org.arhor.photospot.accountanttelegrambot.command.StartCommand
import org.arhor.photospot.accountanttelegrambot.command.StopCommand
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.meta.generics.TelegramBot
import java.lang.invoke.MethodHandles

@SpringBootApplication
class AccountantApplication {

    @Bean
    fun photospotAccountantBot(
        @Value("\${bot.username}") username: String,
        @Value("\${bot.apitoken}") apitoken: String,
        commands: List<IBotCommand>
    ): TelegramBot {
        return PhotospotAccountantBot(username, apitoken, commands)
    }

    @Bean
    fun startCommand(): IBotCommand = StartCommand()

    @Bean
    fun stopCommand(): IBotCommand = StopCommand()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<AccountantApplication>(*args)
        }
    }
}
