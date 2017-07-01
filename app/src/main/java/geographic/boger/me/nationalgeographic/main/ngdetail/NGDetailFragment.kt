package geographic.boger.me.nationalgeographic.main.ngdetail

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.core.DisplayProvider
import geographic.boger.me.nationalgeographic.main.ContentType
import geographic.boger.me.nationalgeographic.main.IActivityMainUIController
import geographic.boger.me.nationalgeographic.main.selectdate.SelectDateAlbumData

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

    private val mIconList by lazy {
        listOf(
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_share_icon),
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_save_icon),
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_fav_icon),
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_icon)
        )
    }

    private val mFontList by lazy {
        listOf(
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_share),
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_save),
                view!!.findViewById<TextView>(R.id.tv_fragment_ng_detail_menu_fav)
        )
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_ng_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresenter.init(this)
        initView()
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
        vpContent.adapter = adapter
        vpContent.pageMargin = DisplayProvider.dp2px(10).toInt()
        vpContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                updateText(adapter.data[position])
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
}