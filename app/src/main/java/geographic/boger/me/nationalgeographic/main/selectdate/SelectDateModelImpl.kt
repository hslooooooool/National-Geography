package geographic.boger.me.nationalgeographic.main.selectdate

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

    private fun mockData(): SelectDateData {
        fun mockAlbumData(count: String): SelectDateAlbumData {
            return SelectDateAlbumData("1614", "2017-06-28 每日精选", "http://pic01.bdatu.com/Upload/appsimg/1497946900.jpg", "2017-06-28 00:04:00", "NO", "YES", "1", "dcbd5a5dccb382f6c2708a1720957624", "1622", "1", "1", "2017-06-28 00:00:00")
        }

        val albumArray = arrayListOf<SelectDateAlbumData>()
        for (j in 1..40) {
            albumArray.add(mockAlbumData(4.toString()))
        }
        return SelectDateData("4", "1", "13", albumArray)
    }

    override fun requestNGDateData(
            pageIdx: Int,
            onError: (Throwable) -> Unit,
            onComplete: () -> Unit,
            onNext: (SelectDateData) -> Unit
    ): Disposable {
        val pageIdxStr: String = pageIdx.toString()
        mSelectDateDataList.forEach {
            if (it.page == pageIdxStr) {
                return Observable.fromArray(it)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(onError, onComplete, onNext)
            }
        }
        return Observable.fromArray(mockData())
                .map {
                    mSelectDateDataList.add(it)
                    return@map it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError, onComplete, onNext)

    }

    fun hasMoreSelectDateData(): Boolean {
        return mSelectDateDataList.size == 0
                || mSelectDateDataList[0].pagecount != mSelectDateDataList.size.toString()
    }
}