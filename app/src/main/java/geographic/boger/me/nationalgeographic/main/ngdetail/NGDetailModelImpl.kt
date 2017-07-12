package geographic.boger.me.nationalgeographic.main.ngdetail

import geographic.boger.me.nationalgeographic.core.NGRumtime
import geographic.boger.me.nationalgeographic.main.selectdate.NGDetailNetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by BogerChan on 2017/6/30.
 */
class NGDetailModelImpl : INGDetailModel {
    private val mService by lazy {
        NGRumtime.retrofit.create(NGDetailNetworkService::class.java)
    }

    private var disposable: Disposable? = null

    override fun requestNGDetailData(id: String,
                                     onStart: () -> Unit,
                                     onError: (Throwable) -> Unit,
                                     onComplete: () -> Unit,
                                     onNext: (NGDetailData) -> Unit): Disposable {
        val dis = mService.requestNGDetailData(id)
                .doOnSubscribe { onStart() }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError, onComplete, onNext)
        disposable = dis
        return dis
    }

    override fun cancelPendingCall() {
        val dis = disposable
        if (dis != null && !dis.isDisposed) {
            dis.dispose()
        }
    }
}