package chrapps.memo.views

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import chrapps.memo.R
import chrapps.memo.models.Card


@SuppressLint("ViewConstructor")
class CardView(id: Int, card: Card, context: Context) : LinearLayout(context) {

    private val mContext: Context = context

    init {
        render(id, card)
    }


    private fun render(id: Int, card: Card) {

        View.inflate(mContext, R.layout.card_view, this)

        // Load content color
        val textColor = card.getContentColor()

        if (textColor == R.color.font_dark_content) {
            findViewById<ImageButton>(R.id.button_card_option).setImageResource(R.drawable.ic_more_black)
        } else {
            findViewById<ImageButton>(R.id.button_card_option).setImageResource(R.drawable.ic_more_white)
        }


        // Set title
        val title: TextView = findViewById(R.id.title)
        title.setTextColor(ContextCompat.getColor(mContext, textColor))
        title.text = card.title

        // Set background view
        val drawableID =  resources.getIdentifier(card.drawable, "drawable", "chrapps.memo")
        setBackgroundResource(drawableID)

        // Assign id for optionButton
        findViewById<ImageButton>(R.id.button_card_option).tag = id

        // Margins
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(20, 10, 20, 20)
        layoutParams = params

        // Append all tasks of this card
        val taskContainer: LinearLayout = findViewById(R.id.tasks_container)


        for (task in card.tasks) {
            val row = LinearLayout(mContext)
            row.orientation = HORIZONTAL

            // Load task text
            val taskView = TextView(mContext)
            taskView.text = task.text
            taskView.setTextColor(ContextCompat.getColor(mContext, textColor))
            taskView.textSize = 8 * resources.displayMetrics.scaledDensity

            // Load checkbox
            val checkBox =
                if (textColor == R.color.font_dark_content) CheckBox(mContext, null, 0, R.style.CheckBoxDark)
                else CheckBox(mContext, null, 0, R.style.CheckBoxLight)

            checkBox.isChecked = task.isChecked

            // Compound task text and checkbox in one row and add it to LinearLayout(taskContainer)
            row.addView(checkBox)
            row.addView(taskView)
            taskContainer.addView(row)
        }

        // Add shadow to card
        elevation = 10.0f

        orientation = VERTICAL
    }



}