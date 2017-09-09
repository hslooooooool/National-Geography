package me.boger.geographic.biz.selectdate

import me.boger.geographic.core.NGRumtime
import java.io.Serializable

/**
 * Created by BogerChan on 2017/6/28.
 */
data class SelectDateData(val total: String, var page: String, var pagecount: String,
                          var album: MutableList<SelectDateAlbumData>) : Serializable

data class SelectDateAlbumData(var id: String, var title: String, var url: String,
                               var addtime: String, var adshow: String, var fabu: String,
                               var encoded: String, var amd5: String, var sort: String,
                               var ds: String, var timing: String, var timingpublish: String) : Serializable {

    @Transient
    private var locale: SelectDateAlbumData? = null

    //can't use lazy prepare, not thread safe
    fun locale(): SelectDateAlbumData {
        if (locale == null) {
            locale = copy(title = NGRumtime.locale(title))
        }
        return locale!!
    }
}