package chrapps.memo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import chrapps.memo.R
import chrapps.memo.models.Card
import chrapps.memo.models.Task
import chrapps.memo.views.CardView

class ListActivity : AppCompatActivity() {

    // UI
    private lateinit var listContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        listContainer = findViewById(R.id.card_container)

        test()
    }


    private fun test() {

        val card = Card("My title", ArrayList(), R.drawable.card_yellow)
        card.tasks.add(Task("My some task #1", true))
        card.tasks.add(Task("My some task #2", false))
        card.tasks.add(Task("My some task #3", true))
        card.tasks.add(Task("My some task #4", false))

        var cardView = CardView(card,this)
        listContainer.addView(cardView)
    }
}
