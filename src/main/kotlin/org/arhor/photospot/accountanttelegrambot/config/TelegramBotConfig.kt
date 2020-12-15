package org.arhor.photospot.accountanttelegrambot.config

import org.arhor.photospot.accountanttelegrambot.command.AddCommand
import org.arhor.photospot.accountanttelegrambot.command.StartCommand
import org.arhor.photospot.accountanttelegrambot.command.StopCommand
import org.arhor.photospot.accountanttelegrambot.service.SheetService
import org.arhor.photospot.accountanttelegrambot.service.impl.PhotospotAccountantBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand
import org.telegram.telegrambots.meta.generics.TelegramBot

@Configuration
class TelegramBotConfig {

    @Bean
    fun photospotAccountantBot(
        @Value("\${telegram.bot.username}") username: String,
        @Value("\${telegram.bot.apitoken}") apitoken: String,
        commands: List<IBotCommand>
    ): TelegramBot {
        return PhotospotAccountantBot(commands, username, apitoken)
    }

    @Bean
    fun helpCommand(): IBotCommand = HelpCommand()

    @Bean
    fun startCommand(): IBotCommand = StartCommand()

    @Bean
    fun stopCommand(): IBotCommand = StopCommand()

    @Bean
    fun addCommand(service: SheetService): IBotCommand = AddCommand(service)
}