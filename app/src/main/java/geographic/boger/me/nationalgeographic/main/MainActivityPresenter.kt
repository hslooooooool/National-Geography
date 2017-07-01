package geographic.boger.me.nationalgeographic.main

/**
 * Created by BogerChan on 2017/6/28.
 */
class MainActivityPresenter {

    private var mView : MainActivity? = null

    fun init(act: MainActivity) {
        act.showSelectDateContent()
        act.setAlbumSelectedListener {
            act.showNGDetailContent(it)
        }
        mView = act
    }
}