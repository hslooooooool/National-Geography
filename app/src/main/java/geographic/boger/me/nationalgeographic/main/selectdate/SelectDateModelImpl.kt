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

    private fun mockData(): SelectDateData {
        fun mockAlbumData(count: String): SelectDateAlbumData {
            return SelectDateAlbumData("1614", "2017-06-28 每日精选", "http://pic01.bdatu.com/Upload/appsimg/1497946900.jpg", "2017-06-28 00:04:00", "NO", "YES", "1", "dcbd5a5dccb382f6c2708a1720957624", "1622", "1", "1", "2017-06-28 00:00:00")
        }

        val albumArray = arrayListOf<SelectDateAlbumData>()
        for (j in 1..10) {
            albumArray.add(mockAlbumData(4.toString()))
        }
        return SelectDateData("4", "1", "13", albumArray)
    }

    override var currentPage: Int = 1

    private var totalPage: Int = 0

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
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(onError, onComplete, onNext)
            }
        }
        return mSelectDateNetworkService.requestNGDateData(pageIdx)
                .doOnNext {
                    mSelectDateDataList.add(it)
                    currentPage = it.page.toInt()
                    totalPage = it.pagecount.toInt()
                    if (currentPage == 1) {
                        it.album.add(0, SelectDateAlbumData.getFavoriteAlbumData())
                    }
                }
                .doOnSubscribe { onStart() }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError, onComplete, onNext)

    }
}