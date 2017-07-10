package geographic.boger.me.nationalgeographic.main.selectdate

import android.view.View
import geographic.boger.me.nationalgeographic.main.ContentType

/**
 * Created by BogerChan on 2017/6/27.
 */
interface ISelectDateUI {

    var contentType: ContentType

    fun getContentView(): View

    fun refreshFavoriteData(favoriteData: SelectDateAlbumData)

    fun refreshCardData(data: List<SelectDateAlbumData>, append: Boolean = false)

    fun finishLoadMore()

    fun finishRefreshing()

    fun setOnRefreshListener(
            onRefresh: (ISelectDateUI) -> Unit,
            onLoadMore: (ISelectDateUI) -> Unit)

    fun setOnRetryClickListener(listener: (view: View) -> Unit)

    fun setEnableLoadMore(canLoadMore: Boolean)
}