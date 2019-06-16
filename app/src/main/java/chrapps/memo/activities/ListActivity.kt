package chrapps.memo.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import chrapps.memo.components.JSONManager
import chrapps.memo.R
import chrapps.memo.models.Storage
import chrapps.memo.views.CardView
import java.text.DateFormatSymbols
import java.util.*


class ListActivity : AppCompatActivity() {

    // UI
    private lateinit var listContainer: LinearLayout
    private lateinit var emptyBoxLabel: LinearLayout

    // Json
    private var jsonManager = JSONManager()

    // Data
    private lateinit var storage: Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTheme()

        // Connect all UI components
        listContainer = findViewById(R.id.card_container)
        emptyBoxLabel = findViewById(R.id.label_empty_box)

        // Read data from .json file
        storage = jsonManager.readStorage(this)

        if (storage.cardMap.size == 0) {
            emptyBoxLabel.visibility = View.VISIBLE
        } else {
            emptyBoxLabel.visibility = View.GONE

            // Get all keys
            val keys = ArrayList<Int>(storage.cardMap.keys)

            // Sort keys [.. 8, 6, 5, 3, 1]
            keys.sortDescending()

            for (key in keys) {
                val card = storage.cardMap[key]
                if (card != null) {
                    val cardView = CardView(key, card,this)
                    listContainer.addView(cardView)
                }

            }
        }

        loadCurrentDate()
    }

    @SuppressLint("SetTextI18n")
    private fun loadCurrentDate() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val monthName = getMonthName(month)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val dayName = getDayName(dayOfWeek)

        findViewById<TextView>(R.id.day_view).text = "$day $monthName"
        findViewById<TextView>(R.id.day_of_week_view).text = dayName
    }

    private fun updateTheme() {
        val themeStyleId = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt(SettingsActivity.THEME_KEY, R.style.CloudAppTheme)

        setTheme(themeStyleId)

        // Load content
        setContentView(R.layout.activity_list)

        // Update buttons with appropriate icons (white ro black)
        when (themeStyleId) {
            R.style.CloudAppTheme -> {
                findViewById<ImageButton>(R.id.button_add).setImageResource(R.drawable.ic_add_black)
                findViewById<ImageButton>(R.id.button_settings).setImageResource(R.drawable.ic_settings_black)
            }

            R.style.LazuriteAppTheme, R.style.UndergroundAppTheme -> {
                findViewById<ImageButton>(R.id.button_add).setImageResource(R.drawable.ic_add_white)
                findViewById<ImageButton>(R.id.button_settings).setImageResource(R.drawable.ic_settings_white)
            }
        }
    }

    fun goToCreateNewCard(view: View) {
        val intent = Intent(this, CardActivity::class.java)
        intent.putExtra(CardActivity.EDIT_CARD_ID, -1)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right)
    }

    fun goToEditCard(view: View) {
        val intent = Intent(this, CardActivity::class.java)
        intent.putExtra(CardActivity.EDIT_CARD_ID, view.tag as Int)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right)
    }

    fun goToSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right)
    }

    private fun getMonthName(month: Int): String {
        return DateFormatSymbols().months[month]
    }

    private fun getDayName(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY    -> "Monday"
            Calendar.TUESDAY   -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY  -> "Thursday"
            Calendar.FRIDAY    -> "Friday"
            Calendar.SATURDAY  -> "Saturday"
            Calendar.SUNDAY    -> "Sunday"
            else -> ""
        }
    }

    override fun onBackPressed() {
        // Put app on the background
        moveTaskToBack(true)
    }
}
