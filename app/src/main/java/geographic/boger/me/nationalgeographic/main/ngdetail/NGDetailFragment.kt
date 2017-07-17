package geographic.boger.me.nationalgeographic.main.ngdetail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import android.widget.Toast
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.core.DisplayProvider
import geographic.boger.me.nationalgeographic.core.NGFragment
import geographic.boger.me.nationalgeographic.core.NGUtil
import geographic.boger.me.nationalgeographic.main.ContentType
import geographic.boger.me.nationalgeographic.main.IActivityMainUIController
import java.io.Serializable
import java.util.*

/**
 * Created by BogerChan on 2017/6/30.
 */
class NGDetailFragment : NGFragment(), INGDetailUI {
    companion object {
        val TAG = "NGDetailFragment"

        val KEY_FRAGMENT_NGDETAIL_INTERNAL_DATA = "key_fragment_ngdetail_internal_data"
    }

    private val mPresenter: INGDetailPresenter by lazy { NGDetailPresenterImpl() }

    private val llcIntroAndMenu by lazy {
        view!!.findViewById<LinearLayoutCompat>(R.id.llc_fragment_ng_detail_intro_and_menu)
    }

    private val llcMenuShare by lazy {
        view!!.findViewById<LinearLayoutCompat>(R.id.llc_fragment_ng_detail_menu_share)
    }

    private val llcMenuSave by lazy {
        view!!.findViewById<LinearLayoutCompat>(R.id.llc_fragment_ng_detail_menu_save)
    }

    private val llcMenuFav by lazy {
        view!!.findViewById<LinearLayoutCompat>(R.id.llc_fragment_ng_detail_menu_fav)
    }

    private val llcMenu by lazy {
        view!!.findViewById<LinearLayoutCompat>(R.id.llc_fragment_ng_detail_menu)
    }

    private val llcLoading by lazy {
        view!!.findViewById<LinearLayoutCompat>(R.id.llc_fragment_ng_detail_loading)
    }

    private val tvTitle by lazy {
        view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_title)
    }

    private val tvPageIdx by lazy {
        view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_page_idx)
    }

    private val tvBody by lazy {
        view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_body)
    }

    private val vpContent by lazy {
        view!!.findViewById<ViewPager>(R.id.vp_fragment_ng_detail)
    }

    private val tvMenuButton by lazy {
        view!!.findViewById<TextView>(R.id.icon_fragment_ng_detail_menu)
    }

    private val tvMenuFavIcon by lazy {
        view!!.findViewById<TextView>(R.id.icon_fragment_ng_detail_menu_fav)
    }

    private val vMenuDivider by lazy {
        view!!.findViewById<View>(R.id.v_fragment_ng_detail_divider_menu)
    }

    private val mMenuDividerList by lazy {
        arrayOf(
                view!!.findViewById<View>(R.id.v_fragment_ng_detail_divider_1),
                view!!.findViewById<View>(R.id.v_fragment_ng_detail_divider_2),
                view!!.findViewById<View>(R.id.v_fragment_ng_detail_divider_3)
        )
    }

    private var mPendingMenuAnimator: Animator? = null

    private var mPendingOverlayAnimator: Animator? = null

    private lateinit var mMainUIController: IActivityMainUIController

    private class InternalData(var id: String? = null,
                               var offlineData: NGDetailData? = null) : Serializable

    private lateinit var mInternalData: InternalData

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_ng_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        restoreDataIfNeed(savedInstanceState)
        mPresenter.restoreDataIfNeed(savedInstanceState)
        initView()
        mPresenter.init(this)
        mMainUIController = activity as IActivityMainUIController
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.destroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        setOverlayMenuShown(true)
    }

    private fun restoreDataIfNeed(savedInstanceState: Bundle?) {
        if (savedInstanceState == null
                || !savedInstanceState.containsKey(KEY_FRAGMENT_NGDETAIL_INTERNAL_DATA)) {
            return
        }
        mInternalData = savedInstanceState.getSerializable(KEY_FRAGMENT_NGDETAIL_INTERNAL_DATA) as InternalData
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mPresenter.onSaveInstanceState(outState)
        if (outState == null) {
            return
        }
        outState.putSerializable(KEY_FRAGMENT_NGDETAIL_INTERNAL_DATA, mInternalData)
    }

    fun initData(id: String?, offlineData: NGDetailData?) {
        mInternalData = InternalData(id, offlineData)
    }

    fun initView() {
        val adapter = NGDetailPageAdapter()
        adapter.setOnItemClickListener(object : NGDetailPageAdapter.OnItemClickListener {

            private var show: Boolean = true

            override fun onItemClick(v: View, position: Int) {
                show = !show
                setOverlayMenuShown(show)
            }
        })
        vpContent.adapter = adapter
        vpContent.pageMargin = DisplayProvider.dp2px(10).toInt()
        vpContent.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {
                updateContent(adapter.data, position)
            }

        })
        llcIntroAndMenu.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        setBottomMenuExpanded(false)
                        llcIntroAndMenu.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }

                })
        tvMenuButton.setOnClickListener(object : View.OnClickListener {

            private var expand: Boolean = false

            override fun onClick(p0: View?) {
                expand = !expand
                setBottomMenuExpanded(expand)
            }

        })
        llcMenuShare.setOnClickListener {
            mPresenter.shareNGDetailImage(
                    (vpContent.adapter as NGDetailPageAdapter).data[vpContent.currentItem].url)
        }
        llcMenuSave.setOnClickListener {
            mPresenter.saveNGDetailImage(
                    (vpContent.adapter as NGDetailPageAdapter).data[vpContent.currentItem].url)
        }
        llcMenuFav.setOnClickListener {
            mPresenter.setNGDetailItemFavoriteState(
                    (vpContent.adapter as NGDetailPageAdapter).data[vpContent.currentItem])
        }
    }

    override var contentType = ContentType.UNSET
        get() {
            return field
        }
        set(value) {
            when (value) {
                ContentType.LOADING -> {
                    llcLoading.visibility = View.VISIBLE
                    llcIntroAndMenu.visibility = View.INVISIBLE
                    vpContent.visibility = View.INVISIBLE
                    tvMenuButton.visibility = View.INVISIBLE
                }
                ContentType.CONTENT -> {
                    llcLoading.visibility = View.INVISIBLE
                    llcIntroAndMenu.visibility = View.VISIBLE
                    vpContent.visibility = View.VISIBLE
                    tvMenuButton.visibility = View.VISIBLE
                }
                ContentType.ERROR -> {
                    Snackbar.make(view, R.string.tip_load_error, Snackbar.LENGTH_SHORT).show()
                    fragmentManager.beginTransaction().remove(this).commit()
                }
                else -> {
                }
            }
            field = value
        }

    override fun refreshData(data: List<NGDetailPictureData>) {
        if (data.isEmpty()) {
            return
        }
        val adapter = vpContent.adapter as NGDetailPageAdapter
        adapter.data = data
        adapter.notifyDataSetChanged()
        vpContent.currentItem = 0
        updateContent(data, 0)
    }

    private fun updateContent(dataList: List<NGDetailPictureData>, idx: Int) {
        val data = dataList[idx]
        val localeData = data.locale()
        tvTitle.text = localeData.title
        tvPageIdx.text = String.format(Locale.US, "%2d/%2d", idx + 1, dataList.size)
        tvBody.text = String.format(Locale.US, getResouceString(R.string.template_detail_text_body), localeData.content, localeData.author)
        setFavoriteButtonState(data.favorite)
    }

    private fun setOverlayMenuShown(show: Boolean) {
        mPendingOverlayAnimator?.cancel()
        val titleBar = mMainUIController.getTitleBar()
        val range = if (show) arrayOf(titleBar.alpha, 1f) else arrayOf(titleBar.alpha, 0f)
        val ani = ValueAnimator.ofFloat(*range.toFloatArray())
        ani.duration = 300
        ani.interpolator = LinearInterpolator()
        ani.addUpdateListener {
            val value = it.animatedValue as Float
            titleBar.alpha = value
            if (isVisible) {
                llcIntroAndMenu.alpha = value
                tvMenuButton.alpha = value
            }
        }
        ani.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                mPendingOverlayAnimator = ani
                if (show) {
                    titleBar.visibility = View.VISIBLE
                    if (isVisible) {
                        llcIntroAndMenu.visibility = View.VISIBLE
                        tvMenuButton.visibility = View.VISIBLE
                    }
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                mPendingOverlayAnimator = null
                if (!show) {
                    titleBar.visibility = View.INVISIBLE
                    if (isVisible) {
                        llcIntroAndMenu.visibility = View.INVISIBLE
                        tvMenuButton.visibility = View.INVISIBLE
                    }
                }
            }
        })
        ani.start()
    }

    private fun setBottomMenuExpanded(expand: Boolean) {
        mPendingMenuAnimator?.cancel()
        val iconText = if (expand) "\ue649" else "\ue6e5"
        val range = if (expand) arrayOf(llcIntroAndMenu.translationY, 0f) else
            arrayOf(llcIntroAndMenu.translationY, (llcIntroAndMenu.height - vMenuDivider.top).toFloat())
        val ani = ValueAnimator.ofFloat(*range.toFloatArray())
        ani.duration = 500
        ani.interpolator = OvershootInterpolator()
        ani.addUpdateListener {
            val value = it.animatedValue as Float
            llcIntroAndMenu.translationY = value
            val fraction = it.animatedFraction
            tvMenuButton.rotation = fraction
            if (fraction > .5f) {
                tvMenuButton.alpha = (fraction - 0.5f) * 2
                if (iconText != tvMenuButton.text) {
                    tvMenuButton.text = iconText
                }
            } else {
                tvMenuButton.alpha = 1 - fraction * 2
            }
        }
        ani.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                mPendingMenuAnimator = ani
                if (!isVisible) {
                    return
                }
                if (expand) {
                    llcMenuShare.visibility = View.VISIBLE
                    llcMenuSave.visibility = View.VISIBLE
                    llcMenuFav.visibility = View.VISIBLE
                    mMenuDividerList.forEach {
                        it.visibility = View.VISIBLE
                    }
                    tvBody.maxLines = Int.MAX_VALUE
                } else {
                    tvBody.maxLines = 4
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                mPendingMenuAnimator = null
                if (!isVisible) {
                    return
                }
                if (!expand) {
                    llcMenuShare.visibility = View.INVISIBLE
                    llcMenuSave.visibility = View.INVISIBLE
                    llcMenuFav.visibility = View.INVISIBLE
                    mMenuDividerList.forEach {
                        it.visibility = View.INVISIBLE
                    }
                }
            }
        })
        ani.start()
    }

    override fun showTipMessage(msg: String) {
        if (NGUtil.isUIThread()) {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        } else {
            activity.runOnUiThread {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun showTipMessage(msgId: Int) {
        Toast.makeText(activity, activity.getString(msgId), Toast.LENGTH_SHORT).show()
    }

    override fun getResouceString(id: Int): String {
        return activity.getString(id)
    }

    override fun getContentResolver(): ContentResolver {
        return activity.contentResolver
    }

    override fun sendBroadcast(intent: Intent) {
        activity.sendBroadcast(intent)
    }

    override fun setFavoriteButtonState(favorite: Boolean) {
        tvMenuFavIcon.text = if (favorite) "\ue677" else "\ue603"
    }

    override fun hasOfflineData(): Boolean {
        return mInternalData.offlineData != null
    }

    override fun getOfflineData(): NGDetailData {
        return mInternalData.offlineData!!
    }

    override fun getNGDetailDataId(): String {
        return mInternalData.id!!
    }
}