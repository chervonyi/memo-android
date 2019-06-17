package chrapps.memo.models

import chrapps.memo.R

class Card(var title: String, var tasks: ArrayList<Task>, var drawable: String) {

    /**
     * Return appropriate content color (white or black) according to 'drawable' field
     */
    fun getContentColor(): Int {
        return when (drawable) {
            "card_yellow", "card_red", "card_green", "card_cyan" -> R.color.font_dark_content
            "card_blue", "card_violet" -> R.color.font_white_content
            else -> R.color.font_dark_content
        }
    }
}