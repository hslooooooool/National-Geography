package geographic.boger.me.nationalgeographic.core

import android.content.Context

/**
 * Created by BogerChan on 2017/6/26.
 */

object DisplayProvider {
    var screenWidth: Int = 0
    var screenHeight: Int = 0

    fun init(context: Context) {
        val metrics = context.applicationContext.resources.displayMetrics
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
    }
}