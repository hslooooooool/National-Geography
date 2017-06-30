package geographic.boger.me.nationalgeographic.main.selectdate

import io.reactivex.disposables.Disposable

/**
 * Created by BogerChan on 2017/6/28.
 */
interface ISelectDateModel {

    fun requestNGDateData(
            pageIdx: Int,
            onStart: () -> Unit,
            onError: (Throwable) -> Unit,
            onComplete: () -> Unit,
            onNext: (SelectDateData) -> Unit
    ): Disposable

    var currentPage: Int

    fun hasNextPage(): Boolean
}