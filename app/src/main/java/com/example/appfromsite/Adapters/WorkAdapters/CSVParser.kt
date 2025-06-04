package com.example.appfromsite.Adapters.WorkAdapters

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.appfromsite.Entity.WorkEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object CsvParser {
    suspend fun parseFromUri(context: Context, uri: Uri): List<WorkEntity> =
        withContext(Dispatchers.IO) {
            parseInputStream(context.contentResolver.openInputStream(uri))
        }

    suspend fun parseFromRaw(context: Context, rawResId: Int): List<WorkEntity> =
        withContext(Dispatchers.IO) {
            parseInputStream(context.resources.openRawResource(rawResId))
        }

    private fun parseInputStream(inputStream: InputStream?): List<WorkEntity> {
        val works = mutableListOf<WorkEntity>()
        if (inputStream == null) return works

        try {
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.forEachLine { line ->
                    try {
                        val parts = line.split(";")
                        if (parts.size >= 3) {
                            works.add(
                                WorkEntity(
                                    id = 0,
                                    name = parts[0].trim(),
                                    size = parts[2].trim().toDoubleOrNull() ?: 0.0,
                                    unit = parts[1].trim()
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("CSVParser", "Error parsing line: $line", e)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("CSVParser", "Error reading file", e)
            throw e
        }
        return works
    }
}