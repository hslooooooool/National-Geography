package geographic.boger.me.nationalgeographic.core

import android.content.Context
import android.support.v7.app.AppCompatActivity

/**
 * Created by BogerChan on 2017/7/11.
 */
open class NGActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(
                LanguageLocalizationHelper.createLocalChangeSupportContext(newBase!!))
    }
}