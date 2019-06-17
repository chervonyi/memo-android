package chrapps.memo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import chrapps.memo.R
import chrapps.memo.views.TaskEditView
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.view.View
import android.widget.*
import chrapps.memo.components.JSONManager
import chrapps.memo.models.Card
import chrapps.memo.models.Task
import android.support.v4.content.ContextCompat
import android.widget.TextView
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.ViewGroup


class CardActivity : AppCompatActivity() {

    // UI
    private lateinit var tasksContainer: LinearLayout
    private lateinit var titleView: TextView
    private lateinit var dummyElement: LinearLayout
    private lateinit var submitButton: ImageButton
    private lateinit var deleteButton: ImageButton

    // Json
    private var jsonManager = JSONManager()

    private var selectedColorId = 0
    private var selectedStyleThemeId: Int = 0

    companion object {
        // Flag for extra of intent
        const val EDIT_CARD_ID = "card_id"
    }

    private var currentCardID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTheme()

        tasksContainer = findViewById(R.id.tasks_container)
        titleView = findViewById(R.id.title_edit_view)
        dummyElement = findViewById(R.id.dummy_id)
        submitButton = findViewById(R.id.button_submit)
        deleteButton = findViewById(R.id.button_delete)

        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        currentCardID = intent.extras.getInt(EDIT_CARD_ID)

        deleteButton.visibility = if(currentCardID != -1) View.VISIBLE else View.INVISIBLE

        titleView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                removeFocus()
            }
            false
        }

        titleView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                checkIfAvailableToSubmit()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

        loadCardInfo()
        setOptionButtonViews()

        // Add additional empty field
        appendTask()

        // Check and show or hide 'Submit' button
        checkIfAvailableToSubmit()
    }


    /**
     * Load appropriate theme according to themeStyleId which saved into via SharedPreferences.
     * Also, according to saved theme, set appropriate image (white or black)
     * for operating buttons (back, submit, delete)
     */
    private fun updateTheme() {
        val themeStyleId = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt(SettingsActivity.THEME_KEY, R.style.CloudAppTheme)

        setTheme(themeStyleId)

        selectedStyleThemeId = themeStyleId

        // Load content
        setContentView(R.layout.activity_card)

        // Update buttons with appropriate icons (white ro black)
        when (themeStyleId) {
            R.style.CloudAppTheme -> {
                findViewById<ImageButton>(R.id.button_back).setImageResource(R.drawable.ic_arrow_back_black)
                findViewById<ImageButton>(R.id.button_submit).setImageResource(R.drawable.ic_check_black)
                findViewById<ImageButton>(R.id.button_delete).setImageResource(R.drawable.ic_delete_black)
            }

            R.style.LazuriteAppTheme, R.style.UndergroundAppTheme -> {
                findViewById<ImageButton>(R.id.button_back).setImageResource(R.drawable.ic_arrow_back_white)
                findViewById<ImageButton>(R.id.button_submit).setImageResource(R.drawable.ic_check_white)
                findViewById<ImageButton>(R.id.button_delete).setImageResource(R.drawable.ic_delete_white)
            }
        }
    }

    /**
     * Reads storage from .json file and set view according to 'currentCardID' from loaded storage.
     */
    private fun loadCardInfo() {
        if (currentCardID != -1) {
            val storage = jsonManager.readStorage(this)
            val card = storage.cardMap[currentCardID]

            if (card != null) {
                titleView.text = card.title

                val drawableId = resources.getIdentifier(card.drawable, "drawable", "chrapps.memo")
                selectedColorId = fromDrawableToID(drawableId)
                findViewById<ImageButton>(fromDrawableToID(drawableId)).setImageResource(R.drawable.ic_check_white)

                for (task in card.tasks) {
                    appendTask(task)
                }
            }
        }
    }

    /**
     * Make square of option buttons (in bottom) using height size which has been set as MATCH_PARENT
     */
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


    /**
     * Listener for 'Submit' button.
     * Method reads all information (title, tasks and selected background color)
     * and calls appropriate method of JSONManager:
     *      appendCard(..)  - if activity opened for creating a new card (currentCardID equals -1)
     *      rewriteCard(..) - if activity opened for editing some exisitng card (currentCardID keeps necessary card id)
     */
    fun onClickSubmit(view: View) {

        val tasks = ArrayList<Task>()

        val childCount = tasksContainer.childCount

        // Read all tasks from 'tasksContainer' (LinearLayout)
        for (id in 0 until childCount) {
            val row = tasksContainer.getChildAt(id)

            if (row is LinearLayout && row.childCount == 2) {
                val checkBox = row.getChildAt(0) as CheckBox
                val editTextView = row.getChildAt(1) as TaskEditView

                if (editTextView.text.isNotEmpty()) {
                    tasks.add(Task(editTextView.text.toString(), checkBox.isChecked))
                }
            }
        }

        // Compound all information in one variable using Card class.
        val card = Card(titleView.text.toString(), tasks,
            resources.getResourceEntryName(fromIDToDrawable(selectedColorId)))

        if (currentCardID == -1) {
            // Creating a new
            jsonManager.appendCard(this, card)

        } else {
            // Editing existing card
            jsonManager.rewriteCard(this, currentCardID, card)
        }

        // Go to ListActivity
        goBack(view)
    }

    /**
     * Listener for 'Delete' button.
     * This method shows some MessageDialog to confirm removing of current card.
     */
    fun onClickDelete(view: View) {

        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    if (currentCardID != -1) {
                        jsonManager.deleteCard(this, currentCardID)
                    }

                    goBack(view)
                }
            }
        }

        val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        builder.setMessage(getString(R.string.question_delete_card))
            .setNegativeButton(getString(R.string.answer_no), dialogClickListener)
            .setPositiveButton(getString(R.string.answer_yes), dialogClickListener)
            .show()
    }

    /**
     * Add one task field to end of the task-listю
     */
    fun appendTask() {
        appendTask(Task("", false))
    }

    /**
     * Add one task field and set data according to given Task instance
     */
    private fun appendTask(task: Task) {

        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL

        val editView = TaskEditView(this)
        editView.setText(task.text)


        // Change cursor
        try {
            val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            f.isAccessible = true
            f.set(editView, 0)
        } catch (ignored: Exception) { }


        var checkBox = CheckBox(this, null, 0, R.style.CheckBoxLight)

        // Change text colors
        when (selectedStyleThemeId) {
            R.style.CloudAppTheme -> {
                editView.setTextColor(ContextCompat.getColor(this, R.color.font_dark_content))
                editView.setHintTextColor(ContextCompat.getColor(this, R.color.hint_cloud))

                checkBox = CheckBox(this, null, 0, R.style.CheckBoxDark)
            }

            R.style.LazuriteAppTheme, R.style.UndergroundAppTheme -> {
                editView.setTextColor(ContextCompat.getColor(this, R.color.font_white_content))
                editView.setHintTextColor(ContextCompat.getColor(this, R.color.hint_underground))
                checkBox = CheckBox(this, null, 0, R.style.CheckBoxLight)
            }
        }

        checkBox.isChecked = task.isChecked
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        val rightMarginForCheckBox = (10 * resources.displayMetrics.density).toInt()
        params.setMargins(0, 0, rightMarginForCheckBox, 0)
        checkBox.layoutParams = params

        // Compound row (checkbox + editView)
        row.addView(checkBox)
        row.addView(editView)

        // Add row to tasksContainer
        tasksContainer.addView(row)
    }

    /**
     * Listener for all option buttons to choice background color.
     * Saves color id of selected button.
     * Remove 'check' image from previous button and then set 'check' image for clicked button.
     */
    fun selectOptionBackgroundColor(view: View) {
        if (selectedColorId != 0) {
            // Remove 'check' image from previous selection
            findViewById<ImageButton>(selectedColorId).setImageResource(0)
        }

        selectedColorId = view.id

        // St 'check' image for selected imageButton
        val drawableName = view.tag as String

        when (resources.getIdentifier(drawableName, "drawable", "chrapps.memo")) {
            R.drawable.option_background_yellow,
            R.drawable.option_background_red,
            R.drawable.option_background_green,
            R.drawable.option_background_cyan -> (view as ImageView).setImageResource(R.drawable.ic_check_black)

            R.drawable.option_background_blue,
            R.drawable.option_background_violet -> (view as ImageView).setImageResource(R.drawable.ic_check_white)
        }

        checkIfAvailableToSubmit()
    }

    /**
     * Convert button id (R.id) to appropriate color id (R.drawable)
     */
    private fun fromIDToDrawable(optionID: Int) : Int {
        when (optionID) {
            R.id.option_yellow  -> return R.drawable.card_yellow
            R.id.option_red     -> return R.drawable.card_red
            R.id.option_green   -> return R.drawable.card_green
            R.id.option_cyan    -> return R.drawable.card_cyan
            R.id.option_blue    -> return R.drawable.card_blue
            R.id.option_violet  -> return R.drawable.card_violet
        }

        return R.drawable.card_yellow
    }

    /**
     * Convert color id (R.drawable) to appropriate button id (R.id)
     */
    private fun fromDrawableToID(colorID: Int) : Int {
        when (colorID) {
            R.drawable.card_yellow  -> return R.id.option_yellow
            R.drawable.card_red     -> return R.id.option_red
            R.drawable.card_green   -> return R.id.option_green
            R.drawable.card_cyan    -> return R.id.option_cyan
            R.drawable.card_blue    -> return R.id.option_blue
            R.drawable.card_violet  -> return R.id.option_violet
        }

        return R.id.option_yellow
    }

    /**
     * Check if task-list contains at least one empty task field
     */
    fun hasEmptyFields(): Boolean {

        val childCount = tasksContainer.childCount

        // Read all tasks from 'tasksContainer' (LinearLayout)
        for (id in 0 until childCount) {
            val row = tasksContainer.getChildAt(id)

            if (row is LinearLayout && row.childCount == 2) {
                val editTextView = row.getChildAt(1) as TaskEditView

                if (editTextView.text.isEmpty()) {
                    return true
                }
            }
        }

        return false
    }

    /**
     * Checks all required state to show or hide 'Submit' button.
     * 'Submit' button will be shown only if:
     *      1. user selected some background color (selectedColorId != 0) AND
     *      2. title field is not empty (titleView.text.length > 0)
     */
    private fun checkIfAvailableToSubmit() {
        val isAvailable: Boolean = selectedColorId != 0 &&
                titleView.text.isNotEmpty()

        submitButton.visibility = if (isAvailable) View.VISIBLE else View.INVISIBLE
    }

    /**
     * Clear focus and hide keyboard.
     */
    fun removeFocus() {
        dummyElement.requestFocus()

        // Hide keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(titleView.windowToken, 0)
    }

    /**
     * Go to MainActivity with slide animation.
     */
    fun goBack(view: View) {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_left)
    }

    override fun onBackPressed() {
        // Put app on the background
        moveTaskToBack(true)
    }
}
