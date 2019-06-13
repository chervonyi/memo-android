package chrapps.memo.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import chrapps.memo.R
import chrapps.memo.views.TaskEditView
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.view.View
import android.widget.*
import chrapps.memo.components.JSONManager


class CardActivity : AppCompatActivity() {

    // UI
    private lateinit var tasksContainer: LinearLayout
    private lateinit var titleView: TextView
    private lateinit var dummyElement: LinearLayout


    // Json
    private var jsonManager = JSONManager()
    
    private var selectedColorId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        tasksContainer = findViewById(R.id.tasks_container)
        titleView = findViewById(R.id.title_edit_view)
        dummyElement = findViewById(R.id.dummy_id)


        titleView.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                removeFocus()
            }
            false
        }

        setOptionButtonViews()
        appendTask()
    }


    private fun setOptionButtonViews() {

        // Some magic to get actual size of height with value of MATCH_PARENT
        val sample = findViewById<ImageButton>(R.id.option_yellow)
        sample.post {
            val height = sample.height

            val optionIds = arrayOf(
                R.id.option_yellow,
                R.id.option_red,
                R.id.option_green,
                R.id.option_cyan,
                R.id.option_blue,
                R.id.option_violet)

            for (id in optionIds) {
                // For each option ImageButton set width as the height (to make a circle)
                val option = findViewById<ImageButton>(id)
                option.layoutParams.width = height
                option.requestLayout()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun appendTask() {

        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL

        val editView = TaskEditView(this)
        editView.requestFocus()

        // Compound row (checkbox + editView)
        row.addView(CheckBox(this))
        row.addView(editView)

        // Add row to tasksContainer
        tasksContainer.addView(row)
    }

    fun selectOptionBackgroundColor(view: View) {
        if (selectedColorId != 0) {
            // Remove 'check' image from previous selection
            findViewById<ImageButton>(selectedColorId).setImageResource(0)
        }

        selectedColorId = view.id

        // St 'check' image for selected imageButton
        (view as ImageButton).setImageResource(R.drawable.ic_check_white)
    }

    fun removeFocus() {
        dummyElement.requestFocus()

        // Hide keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(titleView.windowToken, 0)
    }

    fun goBack(view: View) {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_left)
    }
}
