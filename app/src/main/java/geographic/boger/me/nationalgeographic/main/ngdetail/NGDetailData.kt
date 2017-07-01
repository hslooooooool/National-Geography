package geographic.boger.me.nationalgeographic.main.ngdetail

/**
 * Created by BogerChan on 2017/7/1.
 */

data class NGDetailData(val counttotal: String, val picture: List<NGDetailPictureData>)

data class NGDetailPictureData(val id: String, val albumid: String, val title: String,
                               val content: String, val url: String, val size: String,
                               val addtime: String, val author: String, val thumb: String,
                               val encoded: String, val weburl: String, val type: String,
                               val yourshotlink: String, val copyright: String, val pmd5: String, val sort: String)