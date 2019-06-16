package chrapps.memo.views

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import chrapps.memo.R
import chrapps.memo.activities.CardActivity


class TaskEditView(context: Context?) : EditText(context) {

    private val mContext = context

    init {
        render()
    }

    private fun render() {
        hint = context.getString(R.string.task_hint)
        setBackgroundColor(Color.TRANSPARENT)
        imeOptions = EditorInfo.IME_ACTION_DONE
        inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        textSize = 8 * resources.displayMetrics.scaledDensity

        setSingleLine(false)


        setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (length() > 0 && !(mContext as CardActivity).hasEmptyFields()) {
                    mContext.appendTask()
                } else {
                    (mContext as CardActivity).removeFocus()
                }
            }
            false
        }
    }

}