package chrapps.memo.components

import android.content.Context
import android.preference.PreferenceManager
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

    /**
     * Save given storage into .json file
     */
    private fun saveStorage(context: Context, storage: Storage): Boolean {

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

    /**
     * Read saved instance of Storage class from .json file
     */
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

    /**
     * Main method to work work JSONManager.
     * Creates a new card in Storage and then save it into .json file.
     */
    fun appendCard(context: Context, newCard: Card) {
        rewriteCard(context, getNextIdentificationNumber(context), newCard)
    }

    /**
     * Main method to work work JSONManager.
     * Update existing card with new data and then save it into .json file.
     */
    fun rewriteCard(context: Context, id: Int, card: Card) {
        val currentStorage = readStorage(context)
        currentStorage.cardMap[id] = card
        saveStorage(context, currentStorage)
    }

    /**
     * Main method to work work JSONManager.
     * Deletes excising card from Storage and then save it into .json file.
     */
    fun deleteCard(context: Context, id: Int) {
        val currentStorage = readStorage(context)
        currentStorage.cardMap.remove(id)
        saveStorage(context, currentStorage)
    }

    /**
     * Main method to work work JSONManager.
     * Update task-list in required card in Storage and then save it into .json file.
     */
    fun updateTasks(context: Context, id: Int, newTasks: ArrayList<Task>) {
        val currentStorage = readStorage(context)
        if (currentStorage.cardMap.containsKey(id)) {
            currentStorage.cardMap[id]!!.tasks = newTasks
        }
        saveStorage(context, currentStorage)
    }

    /**
     * Generates and returns unique identifier for a new card.
     *      1. Read the last 'id' from SharedPreferences.
     *      2. Increment this id.
     *      3. Rewrite "last" unique identifier in SharedPreferences.
     *      4. Return id.
     */
    private fun getNextIdentificationNumber(context: Context) : Int {
        val num = PreferenceManager.getDefaultSharedPreferences(context).getInt(UNIQUE_ID_KEY, 1)
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(UNIQUE_ID_KEY, num + 1).apply()
        return num
    }
}