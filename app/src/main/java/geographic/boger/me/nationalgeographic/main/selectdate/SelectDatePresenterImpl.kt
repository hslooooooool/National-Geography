package geographic.boger.me.nationalgeographic.main.selectdate

import android.support.design.widget.Snackbar
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.util.Timber

/**
 * Created by BogerChan on 2017/6/27.
 */
class SelectDatePresenterImpl : ISelectDatePresenter {
    private var mUI: ISelectDateUI? = null

    private val mModel: ISelectDateModel by lazy {
        SelectDateModelImpl()
    }

    override fun init(ui: ISelectDateUI) {
        ui.setOnRetryClickListener({
            Timber.d("Click")
            firstLoadNGData()
        })
        ui.setOnRefreshListener(
                onRefresh = { v ->
                    mModel.requestNGDateData(1,
                            onStart = {},
                            onNext = {
                                mUI?.refreshCardData(it.album)
                            },
                            onError = {
                                Timber.e(it)
                                v.finishRefreshing()
                                Snackbar.make(v.getContentView(), R.string.tip_load_error, Snackbar.LENGTH_SHORT).show()
                            },
                            onComplete = {
                                v.finishRefreshing()
                                v.setEnableLoadMore(mModel.hasNextPage())
                            })
                },
                onLoadMore = { v ->
                    if (!mModel.hasNextPage()) {
                        return@setOnRefreshListener
                    }
                    mModel.requestNGDateData(mModel.currentPage + 1,
                            onStart = {},
                            onNext = {
                                mUI?.refreshCardData(it.album, true)
                            },
                            onError = {
                                Timber.e(it)
                                v.finishLoadMore()
                                Snackbar.make(v.getContentView(), R.string.tip_load_error, Snackbar.LENGTH_SHORT).show()
                            },
                            onComplete = {
                                v.finishLoadMore()
                                v.setEnableLoadMore(mModel.hasNextPage())
                            })
                })
        mUI = ui
        firstLoadNGData()
    }

    private fun firstLoadNGData() {
        mModel.requestNGDateData(1,
                onStart = {
                    mUI?.contentType = ISelectDateUI.ContentType.LOADING
                },
                onNext = {
                    mUI?.refreshCardData(it.album)
                    mUI?.contentType = ISelectDateUI.ContentType.CONTENT
                },
                onError = {
                    Timber.e(it)
                    mUI?.contentType = ISelectDateUI.ContentType.ERROR
                },
                onComplete = {
                    mUI?.setEnableLoadMore(mModel.hasNextPage())
                })
    }
}