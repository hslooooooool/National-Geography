package geographic.boger.me.nationalgeographic.main.selectdate

/**
 * Created by BogerChan on 2017/6/28.
 */
data class SelectDateData(val total: String, var page: String, var pagecount: String,
                          var album: List<SelectDateAlbumData>) {
}

data class SelectDateAlbumData(var id: String, var title: String, var url: String,
                               var addtime: String, var adshow: String, var fabu: String,
                               var encoded: String, var amd5: String, var sort: String,
                               var ds: String, var timing: String, var timingpublish: String)