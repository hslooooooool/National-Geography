package geographic.boger.me.nationalgeographic.biz.selectdate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.util.Timber
import geographic.boger.me.nationalgeographic.view.SealedTextView

/**
 * Created by BogerChan on 2017/6/29.
 */

class SelectDateAdapter(val onItemClickListener: (SelectDateAlbumData) -> Unit) : RecyclerView.Adapter<SelectDateAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sdvContent by lazy { itemView.findViewById(R.id.sdv_item_select_date) as SimpleDraweeView }
        val tvText by lazy { itemView.findViewById(R.id.tv_item_select_date) as SealedTextView }
    }

    var listData: MutableList<SelectDateAlbumData> = mutableListOf()

    private var mPendingScaleAnimator: Animator? = null

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        try {
            holder?.sdvContent?.setImageURI(listData[position].url)
            holder?.tvText?.text = listData[position].locale().title

        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val vh = ViewHolder(LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_select_date, parent, false))
        vh.itemView.setOnClickListener {
            startScale(it, {
                onItemClickListener(listData[vh.adapterPosition])
            }, 1f, 1.05f, 1f)
        }
        return vh
    }

    private fun startScale(view: View, trigger: () -> Unit, vararg range: Float) {
        mPendingScaleAnimator?.cancel()
        val ani = ValueAnimator.ofFloat(*range)
        ani.interpolator = LinearInterpolator()
        ani.duration = 200
        ani.addUpdateListener {
            val value = it.animatedValue as Float
            view.scaleX = value
            view.scaleY = value
        }
        ani.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                mPendingScaleAnimator = animation
            }

            override fun onAnimationEnd(animation: Animator?) {
                trigger()
                mPendingScaleAnimator = null
            }
        })
        ani.start()
    }
}