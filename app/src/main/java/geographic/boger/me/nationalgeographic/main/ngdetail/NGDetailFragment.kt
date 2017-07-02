package geographic.boger.me.nationalgeographic.main.ngdetail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Fragment
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
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.core.DisplayProvider
import geographic.boger.me.nationalgeographic.main.ContentType
import geographic.boger.me.nationalgeographic.main.IActivityMainUIController
import geographic.boger.me.nationalgeographic.main.selectdate.SelectDateAlbumData
import geographic.boger.me.nationalgeographic.util.Timber

/**
 * Created by BogerChan on 2017/6/30.
 */
class NGDetailFragment(val data: SelectDateAlbumData? = null,
                       val controller: IActivityMainUIController? = null) : Fragment(), INGDetailUI {
    companion object {
        val TAG = "NGDetailFragment"
    }

    private val mPresenter: INGDetailPresenter by lazy { NGDetailPresenterImpl(data!!) }

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

    private val tvBody by lazy {
        view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_body)
    }

    private val vpContent by lazy {
        view!!.findViewById<ViewPager>(R.id.vp_fragment_ng_detail)
    }

    private val tvMenuButton by lazy {
        view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_icon)
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

    private val mIconList by lazy {
        arrayOf(
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_share_icon),
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_save_icon),
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_fav_icon),
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_icon)
        )
    }

    private val mFontList by lazy {
        arrayOf(
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_share),
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_save),
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_fav)
        )
    }

    private var mPendingMenuAnimator: Animator? = null

    private var mPendingOverlayAnimator: Animator? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_ng_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresenter.init(this)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        setOverlayMenuShown(true)
    }

    fun initView() {
        tvTitle.typeface = DisplayProvider.primaryTypeface
        tvBody.typeface = DisplayProvider.primaryTypeface
        mIconList.forEach {
            it.typeface = DisplayProvider.iconFont
        }
        mFontList.forEach {
            it.typeface = DisplayProvider.primaryTypeface
        }
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
                updateText(adapter.data[position])
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
                }
                ContentType.CONTENT -> {
                    llcLoading.visibility = View.INVISIBLE
                    llcIntroAndMenu.visibility = View.VISIBLE
                    vpContent.visibility = View.VISIBLE
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
        val adapter = vpContent.adapter as NGDetailPageAdapter
        adapter.data = data
        adapter.notifyDataSetChanged()
        vpContent.currentItem = 0
        updateText(data[0])
    }

    private fun updateText(data: NGDetailPictureData) {
        tvTitle.text = data.title
        tvBody.text = "${data.content} (摄影: ${data.author})"
    }

    private fun setOverlayMenuShown(show: Boolean) {
        mPendingOverlayAnimator?.cancel()
        val titleBar = controller!!.getTitleBar()
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
                if (show) {
                    titleBar.visibility = View.VISIBLE
                    if (isVisible) {
                        llcIntroAndMenu.visibility = View.VISIBLE
                        tvMenuButton.visibility = View.VISIBLE
                    }
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (!show) {
                    titleBar.visibility = View.INVISIBLE
                    if (isVisible) {
                        llcIntroAndMenu.visibility = View.INVISIBLE
                        tvMenuButton.visibility = View.INVISIBLE
                    }
                }
            }
        })
        mPendingOverlayAnimator = ani
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
                mPendingMenuAnimator = null
            }
        })
        mPendingMenuAnimator = ani
        ani.start()
    }
}