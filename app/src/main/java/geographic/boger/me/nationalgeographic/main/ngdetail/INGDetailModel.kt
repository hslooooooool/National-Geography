package geographic.boger.me.nationalgeographic.main.ngdetail

import io.reactivex.disposables.Disposable

/**
 * Created by BogerChan on 2017/7/1.
 */
interface INGDetailModel {
    fun requestNGDetailData(id: String,
                            onStart: () -> Unit,
                            onError: (Throwable) -> Unit,
                            onComplete: () -> Unit,
                            onNext: (NGDetailData) -> Unit): Disposable
}