package geographic.boger.me.nationalgeographic.main

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ContentFrameLayout
import android.widget.TextView
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.core.DisplayProvider
import geographic.boger.me.nationalgeographic.main.selectdate.SelectDateFragment

class MainActivity : AppCompatActivity() {

    private val fragmentSelectDate: Fragment by lazy {
        SelectDateFragment()
    }

    private val mPresenter: MainActivityPresenter by lazy {
        MainActivityPresenter()
    }

    private val tvNGTitle: TextView by lazy {
        findViewById(R.id.tv_activity_main_ng_title) as TextView
    }

    private val tvNGMenu: TextView by lazy {
        findViewById(R.id.tv_activity_main_ng_menu) as TextView
    }

    private val cflContent: ContentFrameLayout by lazy {
        findViewById(R.id.cfl_activity_main_ng_content) as ContentFrameLayout
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

    private fun initView() {
        tvNGTitle.typeface = DisplayProvider.primaryTypeface
        tvNGMenu.typeface = DisplayProvider.iconFont
    }
}
