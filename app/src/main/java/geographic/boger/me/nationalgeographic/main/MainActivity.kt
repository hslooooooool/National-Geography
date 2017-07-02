package geographic.boger.me.nationalgeographic.main

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.core.DisplayProvider
import geographic.boger.me.nationalgeographic.main.ngdetail.NGDetailFragment
import geographic.boger.me.nationalgeographic.main.selectdate.SelectDateAlbumData
import geographic.boger.me.nationalgeographic.main.selectdate.SelectDateFragment

class MainActivity : AppCompatActivity(), IActivityMainUIController {

    private val fragmentSelectDate by lazy {
        SelectDateFragment()
    }

    private val mPresenter by lazy {
        MainActivityPresenter()
    }

    private val tvNGTitle by lazy {
        findViewById(R.id.tv_activity_main_ng_title) as TextView
    }

    private val tvNGMenu by lazy {
        findViewById(R.id.tv_activity_main_ng_menu) as TextView
    }

    private val ablTitle by lazy {
        findViewById(R.id.abl_activity_main_ng_title) as AppBarLayout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        mPresenter.init(this)
    }

    fun showSelectDateContent() {
        fragmentManager.beginTransaction()
                .add(R.id.cfl_activity_main_ng_content, fragmentSelectDate, SelectDateFragment.TAG)
                .commit()
    }

    fun showNGDetailContent(data: SelectDateAlbumData) {
        val df = NGDetailFragment(data, this)
        fragmentManager.beginTransaction()
                .add(R.id.cfl_activity_main_ng_content_full, df, NGDetailFragment.TAG)
                .addToBackStack(null)
                .commit()
        ablTitle.setExpanded(true)
    }

    fun setAlbumSelectedListener(listener: (SelectDateAlbumData) -> Unit) {
        fragmentSelectDate.albumSelectedListener = listener
    }

    private fun initView() {
        tvNGTitle.typeface = DisplayProvider.primaryTypeface
        tvNGMenu.typeface = DisplayProvider.iconFont
        ablTitle.postDelayed({
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }, 800)
    }

    override fun getTitleBar(): View {
        return ablTitle
    }
}
