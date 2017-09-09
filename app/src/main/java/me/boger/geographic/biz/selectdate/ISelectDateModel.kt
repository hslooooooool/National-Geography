package me.boger.geographic.biz.selectdate

import android.os.Bundle
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

    fun cancelPendingCall()

    fun onSaveInstanceState(outState: Bundle?)

    fun restoreDataIfNeed(savedInstanceState: Bundle?)

    fun clearCache()
}