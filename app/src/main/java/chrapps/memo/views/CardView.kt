package chrapps.memo.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import chrapps.memo.R


class CardView : LinearLayout {

    private val mContext: Context

    constructor(context: Context): super(context) {
        mContext = context
        render()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        render()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        render()
    }


    private fun render() {

        View.inflate(mContext, R.layout.card_view, this)

        val title: TextView = findViewById(R.id.title)
        title.text = "My title!"

        setBackgroundResource(R.drawable.card_yellow)

        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(20, 10, 20, 20)
        layoutParams = params



        val taskContainer: LinearLayout = findViewById(R.id.tasks_container)

        val strs = arrayOf("Some task 1", "Some task 2")

        for (str in strs) {
            val row = LinearLayout(mContext)
            row.orientation = HORIZONTAL

            // Customize task text
            val task = TextView(mContext)
            task.text = str
            task.textSize = 8 * resources.displayMetrics.scaledDensity
            task.setTextColor(ContextCompat.getColor(mContext, R.color.font_dark_content))

            val checkBox = CheckBox(mContext, null, 0, R.style.CheckBoxDark)

            row.addView(checkBox)
            row.addView(task)
            taskContainer.addView(row)
        }

        elevation = 10.0f


        orientation = VERTICAL
    }



}