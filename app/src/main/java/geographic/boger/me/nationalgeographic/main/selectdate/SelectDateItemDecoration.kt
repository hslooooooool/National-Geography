package geographic.boger.me.nationalgeographic.main.selectdate

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import geographic.boger.me.nationalgeographic.core.DisplayProvider

/**
 * Created by BogerChan on 2017/6/30.
 */
class SelectDateItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        var dp10 = DisplayProvider.dp2px(10).toInt()
        outRect?.top = dp10 * 3
        outRect?.left = dp10 * 2
        outRect?.right = dp10 * 2
    }
}