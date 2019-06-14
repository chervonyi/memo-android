package chrapps.memo.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import chrapps.memo.components.JSONManager
import chrapps.memo.R
import chrapps.memo.models.Card
import chrapps.memo.models.Storage
import chrapps.memo.models.Task
import chrapps.memo.views.CardView

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


        storage = jsonManager.readStorage(this)

        if (storage.cardMap.size == 0) {
            emptyBoxLabel.visibility = View.VISIBLE
        } else {
            emptyBoxLabel.visibility = View.GONE

            for (card in storage.cardMap) {
                val cardView = CardView(card.key, card.value,this)
                listContainer.addView(cardView)
            }
        }
    }

    private fun updateTheme() {
        val themeStyleId = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt(SettingsActivity.THEME_KEY, R.style.CloudAppTheme)

        setTheme(themeStyleId)

        setContentView(R.layout.activity_list)

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
}
