package geographic.boger.me.nationalgeographic.main.selectdate

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.core.DisplayProvider
import geographic.boger.me.nationalgeographic.util.Timber

/**
 * Created by BogerChan on 2017/6/29.
 */

class SelectDateAdapter : RecyclerView.Adapter<SelectDateAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sdvContent by lazy { itemView.findViewById<SimpleDraweeView>(R.id.sdv_item_select_date) }
        val tvText by lazy { itemView.findViewById<TextView>(R.id.tv_item_select_date) }

        init {
            tvText.typeface = DisplayProvider.primaryTypeface
        }
    }

    var listData: MutableList<SelectDateAlbumData> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        try {
            holder?.sdvContent?.setImageURI(listData[position].url)
            holder?.tvText?.text = listData[position].title
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_select_date, parent, false))
    }
}