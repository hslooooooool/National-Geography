package geographic.boger.me.nationalgeographic.main.ngdetail

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.SimpleDraweeView
import geographic.boger.me.nationalgeographic.R

/**
 * Created by BogerChan on 2017/7/1.
 */
class NGDetailPageAdapter(var data: List<NGDetailPictureData> = emptyList()) : PagerAdapter() {

    private val mIdleViewList by lazy { arrayListOf<SimpleDraweeView>() }
    private val mViewMap by lazy { linkedMapOf<Int, SimpleDraweeView>() }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`

    override fun getCount(): Int = data.size

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val v = if (mIdleViewList.size == 0) SimpleDraweeView(container!!.context).reset()
        else mIdleViewList.removeAt(0)
        mViewMap[position] = v
        v.setImageURI(data[position].url)
        container!!.addView(v,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        return v
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        val v = mViewMap.remove(position)
        mIdleViewList.add(v!!.reset())
        container!!.removeView(v)
    }

    fun SimpleDraweeView.reset(): SimpleDraweeView {
        val h = this.hierarchy
        h.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
        h.setPlaceholderImage(R.drawable.ng_logo_pure, ScalingUtils.ScaleType.CENTER)
        controller = null
        return this
    }
}