package chrapps.memo.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import chrapps.memo.R
import chrapps.memo.models.Card
import chrapps.memo.models.Task
import chrapps.memo.views.CardView

class ListActivity : AppCompatActivity() {

    // UI
    private lateinit var listContainer: LinearLayout
    private lateinit var emptyBoxLabel: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        listContainer = findViewById(R.id.card_container)
        emptyBoxLabel = findViewById(R.id.label_empty_box)


//        emptyBoxLabel.visibility = View.VISIBLE
        test()
    }


    private fun test() {

        val card = Card("My title", ArrayList(), R.drawable.card_violet)
        card.tasks.add(Task("My some task #1", true))
        card.tasks.add(Task("My some task #2", false))
        card.tasks.add(Task("My some task #3", true))
        card.tasks.add(Task("My some task #4", false))

        var cardView = CardView(card,this)
        listContainer.addView(cardView)
    }

    fun goToCreateNewCard(view: View) {
        val intent = Intent(this, CardActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right)
    }

    fun goToEditCard(view: View) {
        val intent = Intent(this, CardActivity::class.java)
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
