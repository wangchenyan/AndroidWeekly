package me.wcy.androidweekly.fragment

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import me.wcy.androidweekly.BuildConfig
import me.wcy.androidweekly.R
import me.wcy.androidweekly.activity.BrowserActivity

class SettingFragment : PreferenceFragment(), Preference.OnPreferenceClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference_setting)

        val version = findPreference("key_version")
        val source = findPreference("key_source")
        version.summary = BuildConfig.VERSION_NAME
        source.onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference!!.key) {
            "key_source" -> {
                val url = "https://github.com/wangchenyan/AndroidWeekly"
                BrowserActivity.start(activity, url)
                return true
            }
        }
        return false
    }
}