package chrapps.memo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import chrapps.memo.R
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

        var cardView = CardView(this)
        listContainer.addView(cardView)
    }
}
