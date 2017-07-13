package geographic.boger.me.nationalgeographic.main.selectdate

import geographic.boger.me.nationalgeographic.core.NGRumtime
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by BogerChan on 2017/6/28.
 */

class SelectDateModelImpl : ISelectDateModel {

    private val mSelectDateDataList = arrayListOf<SelectDateData>()
    private val mSelectDateNetworkService by lazy {
        NGRumtime.retrofit.create(SelectDateNetworkService::class.java)
    }

    override var currentPage: Int = 1

    private var totalPage: Int = 0

    private val mPendingCall = mutableListOf<Disposable>()

    override fun hasNextPage(): Boolean {
        return currentPage < totalPage
    }

    override fun requestNGDateData(
            pageIdx: Int,
            onStart: () -> Unit,
            onError: (Throwable) -> Unit,
            onComplete: () -> Unit,
            onNext: (SelectDateData) -> Unit
    ): Disposable {
        val pageIdxStr: String = pageIdx.toString()
        mSelectDateDataList.forEach {
            if (it.page == pageIdxStr) {
                return Observable.just(it)
                        .doOnNext {
                            currentPage = it.page.toInt()
                            totalPage = it.pagecount.toInt()
                            if (currentPage == 1) {
                                it.album[0] = NGRumtime.favoriteNGDataSupplier.getFavoriteAlbumData()
                            }
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(onError, onComplete, onNext)
            }
        }
        var disposable: Disposable? = null
        disposable = mSelectDateNetworkService.requestNGDateData(pageIdx)
                .doOnNext {
                    mSelectDateDataList.add(it)
                    currentPage = it.page.toInt()
                    totalPage = it.pagecount.toInt()
                    if (currentPage == 1) {
                        it.album.add(0, NGRumtime.favoriteNGDataSupplier.getFavoriteAlbumData())
                    }
                }
                .doOnSubscribe { onStart() }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        {
                            onError(it)
                            mPendingCall.remove(disposable)
                        },
                        {
                            onComplete()
                            mPendingCall.remove(disposable)
                        },
                        onNext)
        mPendingCall.add(disposable)
        return disposable
    }


    override fun cancelPendingCall() {
        mPendingCall.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mPendingCall.clear()
    }
}