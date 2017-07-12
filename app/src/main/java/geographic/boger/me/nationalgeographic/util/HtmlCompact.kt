package geographic.boger.me.nationalgeographic.util

import android.os.Build
import android.text.Html

/**
 * Created by BogerChan on 2017/7/12.
 */
object HtmlCompact {
    fun fromHtml(source: String) =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) Html.fromHtml(source)
            else Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
}