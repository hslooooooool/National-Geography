package geographic.boger.me.nationalgeographic.main.selectdate

import android.view.View

/**
 * Created by BogerChan on 2017/6/27.
 */
interface ISelectDateUI {
    fun getContentView(): View

    fun refreshCardData(data: List<SelectDateAlbumData>)
}