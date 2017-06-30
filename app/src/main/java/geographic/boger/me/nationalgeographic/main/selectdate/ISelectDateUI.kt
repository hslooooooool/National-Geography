package geographic.boger.me.nationalgeographic.main.selectdate

import android.view.View

/**
 * Created by BogerChan on 2017/6/27.
 */
interface ISelectDateUI {

    enum class ContentType {
        UNSET, CONTENT, LOADING, ERROR
    }

    var contentType: ContentType

    fun getContentView(): View

    fun refreshCardData(data: List<SelectDateAlbumData>, append: Boolean = false)

    fun finishLoadMore()

    fun finishRefreshing()

    fun setOnRefreshListener(
            onRefresh: (ISelectDateUI) -> Unit,
            onLoadMore: (ISelectDateUI) -> Unit)

    fun setOnRetryClickListener(listener: (view: View) -> Unit)

    fun setEnableLoadMore(canLoadMore: Boolean)
}