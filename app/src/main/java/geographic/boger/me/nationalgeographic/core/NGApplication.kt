package geographic.boger.me.nationalgeographic.core

import android.app.Application
import android.text.TextUtils
import com.facebook.drawee.backends.pipeline.Fresco
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport
import geographic.boger.me.nationalgeographic.BuildConfig
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.main.MainActivity
import geographic.boger.me.nationalgeographic.util.Timber

/**
 * Created by BogerChan on 2017/6/25.
 */
class NGApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLog()
        Fresco.initialize(this)
        DisplayProvider.init(this)
        NGRumtime.init(this)
        LanguageLocalizationHelper.init(this)
        initBugly()
    }

    private fun initBugly() {
        val appKeyBugly = getString(R.string.app_id_bugly)
        if (!TextUtils.isEmpty(appKeyBugly)) {
            Bugly.init(this, appKeyBugly, BuildConfig.DEBUG)
        }
        Beta.canShowUpgradeActs.add(MainActivity::class.java)
        Beta.autoDownloadOnWifi = true
        Beta.autoCheckUpgrade = true
        CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG)
    }

    private fun initLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}