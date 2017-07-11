package geographic.boger.me.nationalgeographic.main.ngdetail

/**
 * Created by BogerChan on 2017/7/1.
 */

data class NGDetailData(var counttotal: String, var picture: MutableList<NGDetailPictureData>)

data class NGDetailPictureData(val id: String, val albumid: String, var title: String,
                               var content: String, val url: String, val size: String,
                               val addtime: String, var author: String, val thumb: String,
                               val encoded: String, val weburl: String, val type: String,
                               val yourshotlink: String, val copyright: String, val pmd5: String,
                               val sort: String, var favorite: Boolean = false) {
    override fun equals(other: Any?): Boolean {
        return other is NGDetailPictureData && id == other.id
    }
}