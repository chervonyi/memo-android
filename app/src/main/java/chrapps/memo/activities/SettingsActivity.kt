package chrapps.memo.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import chrapps.memo.R

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val THEME_KEY = "selected_theme"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTheme()
    }

    private fun updateTheme() {
        val themeStyleId = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt(THEME_KEY, R.style.CloudAppTheme)

        setTheme(themeStyleId)

        // Load content
        setContentView(R.layout.activity_settings)

        // Update buttons with appropriate icons (white ro black)
        when (themeStyleId) {
            R.style.CloudAppTheme -> {
                findViewById<ImageButton>(R.id.button_back).setImageResource(R.drawable.ic_arrow_back_black)
            }

            R.style.LazuriteAppTheme, R.style.UndergroundAppTheme -> {
                findViewById<ImageButton>(R.id.button_back).setImageResource(R.drawable.ic_arrow_back_white)
            }
        }
    }

    fun goBack(view: View) {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_left)
    }

    fun changeTheme(view: View) {
        when (view.id) {
            R.id.button_theme_cloud -> setNewTheme(R.style.CloudAppTheme)
            R.id.button_theme_lazurite -> setNewTheme(R.style.LazuriteAppTheme)
            R.id.button_theme_underground -> setNewTheme(R.style.UndergroundAppTheme)
        }
    }

    private fun setNewTheme(themeStyleId: Int) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(THEME_KEY, themeStyleId).apply()

        finish()
        startActivity(intent)
    }

    override fun onBackPressed() {
        // Put app on the background
        moveTaskToBack(true)
    }
}
