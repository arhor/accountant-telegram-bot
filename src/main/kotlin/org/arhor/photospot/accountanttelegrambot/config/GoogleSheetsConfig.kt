package org.arhor.photospot.accountanttelegrambot.config

import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.Base64
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import org.arhor.photospot.accountanttelegrambot.service.SheetService
import org.arhor.photospot.accountanttelegrambot.service.impl.GoogleSheetService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GoogleSheetsConfig {

    @Bean
    fun httpRequestInitializer(
        @Value("\${google.service-account}") secretEncoded: String
    ): HttpRequestInitializer {
        // since JSON format contains lots of special characters the easiest way to
        // store in environment variable is to encode it using `Base 64` encoding.
        val secret = Base64.decodeBase64(secretEncoded)

        val credentials =
            GoogleCredentials
                .fromStream(secret.inputStream())
                .createScoped(setOf(SheetsScopes.SPREADSHEETS))

        return HttpCredentialsAdapter(credentials)
    }

    @Bean
    fun sheetsApi(
        @Value("\${google.app-name}") appName: String,
        httpRequestInitializer: HttpRequestInitializer
    ): Sheets {
        val httpTransport = NetHttpTransport.Builder().build()
        val jsonFactory = JacksonFactory.getDefaultInstance()

        return Sheets
            .Builder(httpTransport, jsonFactory, httpRequestInitializer)
            .setApplicationName(appName)
            .build()
    }

    @Bean
    fun googleSheetsService(
        @Value("\${google.sheet-id}") sheetId: String,
        @Value("\${google.tab-name}") tabName: String,
        sheetsApi: Sheets
    ): SheetService {
        return GoogleSheetService(sheetId, tabName, sheetsApi)
    }
}