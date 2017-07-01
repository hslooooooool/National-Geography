package geographic.boger.me.nationalgeographic.main.selectdate

import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.footer.BallPulseView
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.core.DisplayProvider
import geographic.boger.me.nationalgeographic.main.ContentType
import jp.wasabeef.recyclerview.adapters.AnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator

/**
 * Created by BogerChan on 2017/6/27.
 */
class SelectDateFragment : Fragment(), ISelectDateUI {

    companion object {
        val TAG = "SelectDateFragment"
    }

    private val mPresenter: ISelectDatePresenter by lazy {
        SelectDatePresenterImpl()
    }

    private val rvContent: RecyclerView by lazy {
        view!!.findViewById<RecyclerView>(R.id.rv_fragment_select_date)
    }

    private val trlContent by lazy {
        view!!.findViewById<TwinklingRefreshLayout>(R.id.trl_select_date)
    }

    private val tvLoading by lazy {
        view!!.findViewById<TextView>(R.id.tv_fragment_select_date_loading)
    }

    private val llLoading by lazy {
        view!!.findViewById<LinearLayoutCompat>(R.id.ll_fragment_select_date_loading)
    }

    private val llError by lazy {
        view!!.findViewById<LinearLayoutCompat>(R.id.ll_fragment_select_date_error)
    }

    private val tvError by lazy {
        view!!.findViewById<TextView>(R.id.tv_fragment_select_date_error)
    }

    private val tvErrorIcon by lazy {
        view!!.findViewById<TextView>(R.id.tv_fragment_select_date_error_icon)
    }

    override var contentType = ContentType.UNSET
        get() {
            return field
        }
        set(value) {
            when (value) {
                ContentType.LOADING -> {
                    llLoading.visibility = View.VISIBLE
                    llError.visibility = View.INVISIBLE
                    trlContent.visibility = View.INVISIBLE
                }
                ContentType.CONTENT -> {
                    llLoading.visibility = View.INVISIBLE
                    llError.visibility = View.INVISIBLE
                    trlContent.visibility = View.VISIBLE
                }
                ContentType.ERROR -> {
                    llLoading.visibility = View.INVISIBLE
                    llError.visibility = View.VISIBLE
                    trlContent.visibility = View.INVISIBLE
                }
                else -> {
                }
            }
            field = value
        }

    var albumSelectedListener: (SelectDateAlbumData) -> Unit = {}

    override fun setOnRetryClickListener(listener: (view: View) -> Unit) {
        llError.setOnClickListener(listener)
    }

    override fun setEnableLoadMore(canLoadMore: Boolean) {
        trlContent.setEnableLoadmore(canLoadMore)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_select_date, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        mPresenter.init(this)
    }

    fun initViews() {
        rvContent.adapter = SlideInBottomAnimationAdapter(SelectDateAdapter(albumSelectedListener))
        rvContent.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvContent.itemAnimator = LandingAnimator()
        rvContent.addItemDecoration(SelectDateItemDecoration())
        val bezierHeaderView = BezierLayout(activity)
        bezierHeaderView.setWaveColor(ResourcesCompat.getColor(resources, R.color.color_gray_50, activity.theme))
        bezierHeaderView.setRippleColor(ResourcesCompat.getColor(resources, R.color.ng_yellow_50, activity.theme))
        trlContent.setHeaderView(bezierHeaderView)
        val ballPulseView = BallPulseView(activity)
        ballPulseView.setNormalColor(ResourcesCompat.getColor(resources, R.color.ng_yellow_50, activity.theme))
        ballPulseView.setAnimatingColor(ResourcesCompat.getColor(resources, R.color.ng_yellow_50, activity.theme))
        trlContent.setBottomView(ballPulseView)
        tvLoading.typeface = DisplayProvider.primaryTypeface
        tvError.typeface = DisplayProvider.primaryTypeface
        tvErrorIcon.typeface = DisplayProvider.iconFont
    }

    override fun getContentView(): View {
        return trlContent
    }

    override fun refreshCardData(data: List<SelectDateAlbumData>, append: Boolean) {
        val adapter = (rvContent.adapter as AnimationAdapter).wrappedAdapter as SelectDateAdapter
        if (append) adapter.listData.addAll(data)
        else adapter.listData = data.toMutableList()
        adapter.notifyDataSetChanged()
    }

    override fun finishLoadMore() {
        trlContent.finishLoadmore()
    }

    override fun finishRefreshing() {
        trlContent.finishRefreshing()
    }

    override fun setOnRefreshListener(
            onRefresh: (ISelectDateUI) -> Unit,
            onLoadMore: (ISelectDateUI) -> Unit) {
        trlContent.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                onRefresh(this@SelectDateFragment)
            }

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                onLoadMore(this@SelectDateFragment)
            }
        })
    }
}