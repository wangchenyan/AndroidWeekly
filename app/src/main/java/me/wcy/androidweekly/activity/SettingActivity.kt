package me.wcy.androidweekly.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import me.wcy.androidweekly.R
import me.wcy.androidweekly.fragment.SettingFragment

class SettingActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val settingFragment = SettingFragment()
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, settingFragment)
                .commit()
    }
}
