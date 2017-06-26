package geographic.boger.me.nationalgeographic.core

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by BogerChan on 2017/6/25.
 */
class NGApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        DisplayProvider.init(this)
    }
}