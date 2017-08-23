package geographic.boger.me.nationalgeographic.biz.selectdate

import android.os.Bundle

/**
 * Created by BogerChan on 2017/6/27.
 */
interface ISelectDatePresenter {
    fun init(ui: ISelectDateUI)

    fun notifyFavoriteNGDetailDataChanged()

    fun destroy()

    fun onSaveInstanceState(outState: Bundle?)

    fun restoreDataIfNeed(savedInstanceState: Bundle?)

}