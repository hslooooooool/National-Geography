package geographic.boger.me.nationalgeographic.main.selectdate

import android.support.design.widget.Snackbar
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.util.Timber

/**
 * Created by BogerChan on 2017/6/27.
 */
class SelectDatePresenterImpl : ISelectDatePresenter {
    private var mUI : ISelectDateUI? = null

    private val mModel: ISelectDateModel by lazy {
        SelectDateModelImpl()
    }

    override fun init(ui : ISelectDateUI) {
        mUI = ui
        initModel()
    }

    private fun initModel() {
        mModel.requestNGDateData(1,
                onError = {
                    Timber.e(it)
                    Snackbar.make(mUI!!.getContentView(), R.string.tip_load_error, Snackbar.LENGTH_SHORT).show()
                },
                onComplete = {},
                onNext = {
                    Timber.d("数据请求成功")
                    mUI!!.refreshCardData(it.album)
                })
    }

}