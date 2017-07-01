package geographic.boger.me.nationalgeographic.main.ngdetail

import geographic.boger.me.nationalgeographic.main.ContentType

/**
 * Created by BogerChan on 2017/6/30.
 */
interface INGDetailUI {
    var contentType: ContentType

    fun refreshData(data: List<NGDetailPictureData>)
}