package geographic.boger.me.nationalgeographic.main.selectdate

import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.core.NGRumtime

/**
 * Created by BogerChan on 2017/6/28.
 */
data class SelectDateData(val total: String, var page: String, var pagecount: String,
                          var album: MutableList<SelectDateAlbumData>)

data class SelectDateAlbumData(var id: String, var title: String, var url: String,
                               var addtime: String, var adshow: String, var fabu: String,
                               var encoded: String, var amd5: String, var sort: String,
                               var ds: String, var timing: String, var timingpublish: String) {

    companion object {
        fun getFavoriteAlbumData(): SelectDateAlbumData {
            val data = NGRumtime.favoriteNGDetailDataSupplier.getNGDetailData().picture
            val url = if (data.size > 0) data[data.size - 1].url else ""

            return SelectDateAlbumData(
                    "unset",
                    "${NGRumtime.application.getString(R.string.text_favorite_item_title)} (共${data.size}张)",
                    url, "unset", "unset", "unset", "unset", "unset", "unset",
                    "unset", "unset", "unset")
        }
    }
}