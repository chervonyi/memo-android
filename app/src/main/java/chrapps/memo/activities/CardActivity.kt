package chrapps.memo.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import chrapps.memo.R
import chrapps.memo.views.TaskEditView
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.content.Context
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager


class CardActivity : AppCompatActivity() {

    // UI
    private lateinit var tasksContainer: LinearLayout
    private lateinit var titleView: TextView
    private lateinit var dummyElement: LinearLayout

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

        appendTask()
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

    fun removeFocus() {
        dummyElement.requestFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(titleView.windowToken, 0)
    }
}
