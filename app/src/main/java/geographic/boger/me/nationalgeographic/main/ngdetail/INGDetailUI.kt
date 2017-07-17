package geographic.boger.me.nationalgeographic.main.ngdetail

import android.content.ContentResolver
import android.content.Intent
import geographic.boger.me.nationalgeographic.main.ContentType

/**
 * Created by BogerChan on 2017/6/30.
 */
interface INGDetailUI {
    var contentType: ContentType

    fun refreshData(data: List<NGDetailPictureData>)

    fun showTipMessage(msg: String)

    fun showTipMessage(msgId: Int)

    fun startActivity(intent: Intent)

    fun getResouceString(id: Int): String

    fun getContentResolver(): ContentResolver

    fun sendBroadcast(intent: Intent)

    fun setFavoriteButtonState(favorite: Boolean)

    fun hasOfflineData(): Boolean

    fun getOfflineData(): NGDetailData

    fun getNGDetailDataId(): String
}