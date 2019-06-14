package chrapps.memo.components

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import chrapps.memo.R
import chrapps.memo.models.Card
import chrapps.memo.models.Storage
import chrapps.memo.models.Task
import com.google.gson.Gson
import java.io.*

class JSONManager {

    @Suppress("PrivatePropertyName")
    private val FILENAME = "cards.json"

    private val UNIQUE_ID_KEY = "CARD_ID"

    private val gson = Gson()

    fun saveStorage(context: Context, storage: Storage): Boolean {

        val jsonString = gson.toJson(storage)

        return try {
            val fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)
            if (jsonString != null) {
                fos.write(jsonString.toByteArray())
            }
            fos.close()
            true
        } catch (fileNotFound: FileNotFoundException) {
            false
        } catch (ioException: IOException) {
            false
        }

    }

    fun readStorage(context: Context): Storage {
        try {
            val fis = context.openFileInput(FILENAME)
            val isr = InputStreamReader(fis)
            val bufferedReader = BufferedReader(isr)
            val sb = StringBuilder()

            var line = bufferedReader.readLine()

            while (line != null) {
                sb.append(line)
                line = bufferedReader.readLine()
            }

            return gson.fromJson(sb.toString(), Storage::class.java)

        } catch (fileNotFound: FileNotFoundException) {
            return Storage()
        } catch (ioException: IOException) {
            return Storage()
        }

    }

    fun appendCard(context: Context, newCard: Card) {
        rewriteCard(context, getNextIdentificationNumber(context), newCard)
    }

    fun rewriteCard(context: Context, id: Int, card: Card) {
        val currentStorage = readStorage(context)

        currentStorage.cardMap[id] = card

        saveStorage(context, currentStorage)
    }

    private fun getNextIdentificationNumber(context: Context) : Int {
        val num = PreferenceManager.getDefaultSharedPreferences(context).getInt(UNIQUE_ID_KEY, 1)
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(UNIQUE_ID_KEY, num + 1).apply()
        return num
    }
}