package geographic.boger.me.nationalgeographic.core

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import geographic.boger.me.nationalgeographic.BuildConfig
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
    }

    private fun initLog() {
//        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
//        }
    }
}