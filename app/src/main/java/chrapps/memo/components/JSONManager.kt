package chrapps.memo.components

import android.content.Context
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

    fun isFilePresent(context: Context): Boolean {
        val path = context.filesDir.absolutePath + "/" + FILENAME
        val file = File(path)
        return file.exists()
    }
}