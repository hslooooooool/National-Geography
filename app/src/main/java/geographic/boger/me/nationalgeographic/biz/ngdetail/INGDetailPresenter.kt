package geographic.boger.me.nationalgeographic.biz.ngdetail

import android.os.Bundle

/**
 * Created by BogerChan on 2017/6/30.
 */
interface INGDetailPresenter {
    fun init(ui: INGDetailUI)

    fun shareNGDetailImage(url: String)

    fun saveNGDetailImage(url: String)

    fun setNGDetailItemFavoriteState(data: NGDetailPictureData)

    fun destroy()

    fun onSaveInstanceState(outState: Bundle?)

    fun restoreDataIfNeed(savedInstanceState: Bundle?)
}