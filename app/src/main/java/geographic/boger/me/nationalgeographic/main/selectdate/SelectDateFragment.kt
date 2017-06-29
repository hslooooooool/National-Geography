package geographic.boger.me.nationalgeographic.main.selectdate

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import geographic.boger.me.nationalgeographic.R
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_select_date, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        mPresenter.init(this)
    }

    fun initViews() {
        rvContent.adapter = SlideInBottomAnimationAdapter(SelectDateAdapter())
        rvContent.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvContent.itemAnimator = LandingAnimator()
        rvContent.addItemDecoration(SelectDateItemDecoration())
    }

    override fun getContentView(): View {
        return rvContent
    }

    override fun refreshCardData(data: List<SelectDateAlbumData>) {
        ((rvContent.adapter as AnimationAdapter).wrappedAdapter as SelectDateAdapter).setData(data)
    }

}