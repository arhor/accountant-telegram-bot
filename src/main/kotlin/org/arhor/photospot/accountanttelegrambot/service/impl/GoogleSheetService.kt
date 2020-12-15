package org.arhor.photospot.accountanttelegrambot.service.impl

import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.AppendValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import org.arhor.photospot.accountanttelegrambot.core.ActionResult
import org.arhor.photospot.accountanttelegrambot.core.ActionResult.Companion.actionResult
import org.arhor.photospot.accountanttelegrambot.service.SheetService

class GoogleSheetService(
    private val sheetId: String,
    private val tabName: String,
    private val sheetsApi: Sheets
) : SheetService {

    override fun append(range: String, value: Any, vararg other: Any) : ActionResult<String> {
        return actionResult {
            appendInternal(
                query(range),
                dataRow(value, *other)
            )
            "Record `${value}` was added to the table"
        }
    }

    private fun query(range: String): String = "'${tabName}'!${range}"

    private fun dataRow(vararg values: Any): ValueRange {
        return ValueRange().setValues(listOf(values.toList()))
    }

    private fun appendInternal(range: String, values: ValueRange): AppendValuesResponse? {
        return sheetsApi.spreadsheets()
            .values()
            .append(sheetId, range, values)
            .setValueInputOption("RAW")
            .setInsertDataOption("INSERT_ROWS")
            .execute()
    }
}