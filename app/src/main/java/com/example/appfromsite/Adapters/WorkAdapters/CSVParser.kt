package com.example.appfromsite.Adapters.WorkAdapters

import android.content.Context
import com.example.appfromsite.Entity.WorkEntity
import java.io.BufferedReader
import java.io.InputStreamReader

class CSVParser {
    fun parseWorks(context: Context, resId: Int): List<WorkEntity> {
        return context.resources.openRawResource(resId).use { stream ->
            BufferedReader(InputStreamReader(stream)).useLines { lines ->
                lines.mapNotNull { parseLine(it) }.toList()
            }
        }
    }

    private fun parseLine(line: String): WorkEntity? {
        return try {
            val processed = line.replace("\"\"", "\"")
            val parts = processed.split(";")
            if (parts.size == 3) {
                WorkEntity(
                    id = 0,
                    name = parts[0].trim(),
                    size = parts[2].trim().toDouble(),
                    unit = parts[1].trim()
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }
}