package geographic.boger.me.nationalgeographic.main.ngdetail

import geographic.boger.me.nationalgeographic.main.ContentType
import geographic.boger.me.nationalgeographic.main.selectdate.SelectDateAlbumData

/**
 * Created by BogerChan on 2017/6/30.
 */

class NGDetailPresenterImpl(val data: SelectDateAlbumData) : INGDetailPresenter {

    private var mUI: INGDetailUI? = null

    private val mModel: INGDetailModel by lazy {
        NGDetailModelImpl()
    }

    override fun init(ui: INGDetailUI) {
        mModel.requestNGDetailData(data.id,
                onStart = {
                    ui.contentType = ContentType.LOADING
                },
                onError = {
                    ui.contentType = ContentType.ERROR
                },
                onComplete = {
                    ui.contentType = ContentType.CONTENT
                },
                onNext = {
                    ui.refreshData(it.picture)
                })
        mUI = ui
    }
}