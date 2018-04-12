package me.wcy.androidweekly.fragment

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import me.wcy.androidweekly.BuildConfig
import me.wcy.androidweekly.R
import me.wcy.androidweekly.activity.BrowserActivity
import me.wcy.androidweekly.application.VersionManager

class SettingFragment : PreferenceFragment(), Preference.OnPreferenceClickListener {
    private var version: Preference? = null
    private var update: Preference? = null
    private var source: Preference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference_setting)

        version = findPreference("key_version")
        update = findPreference("key_update")
        source = findPreference("key_source")
        version!!.summary = "v${BuildConfig.VERSION_NAME}"
        update!!.onPreferenceClickListener = this
        source!!.onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference) {
            update -> {
                VersionManager.get().checkVersion(activity, false)
                return true
            }
            source -> {
                val url = "https://github.com/wangchenyan/AndroidWeekly"
                BrowserActivity.start(activity, url)
                return true
            }
        }
        return false
    }
}