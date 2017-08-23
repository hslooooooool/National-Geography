package geographic.boger.me.nationalgeographic.biz.common

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import com.google.gson.reflect.TypeToken
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.core.NGRumtime
import geographic.boger.me.nationalgeographic.biz.ngdetail.NGDetailData
import geographic.boger.me.nationalgeographic.biz.ngdetail.NGDetailPictureData
import geographic.boger.me.nationalgeographic.biz.selectdate.SelectDateAlbumData
import geographic.boger.me.nationalgeographic.util.Timber
import java.util.*

/**
 * Created by BogerChan on 2017/7/10.
 */
class FavoriteNGDataSupplier(val ctx: Context) {

    companion object {
        val KEY_FAVORITE_NG_DETAIL_DATA = "fav_ng_detail_data"

        val ACTION_NG_DETAIL_DATA_CHANGED = "geographic.boger.me.nationalgeographic.ACTION_NG_DETAIL_DATA"
    }

    private var mNGDetailData: NGDetailData = NGDetailData("0", ArrayList<NGDetailPictureData>(0))

    private var mSP = ctx.getSharedPreferences(ctx.packageName, Context.MODE_PRIVATE)

    init {
        val jsonNGDetailData = mSP.getString(KEY_FAVORITE_NG_DETAIL_DATA, null)
        if (!TextUtils.isEmpty(jsonNGDetailData)) {
            val list = NGRumtime.gson.fromJson<MutableList<NGDetailPictureData>>(
                    jsonNGDetailData,
                    object : TypeToken<MutableList<NGDetailPictureData>>() {}.type)
            mNGDetailData.counttotal = list.size.toString()
            mNGDetailData.picture = list
        }
    }

    fun addNGDetailDataToFavorite(data: NGDetailPictureData): Boolean {
        try {
            if (mNGDetailData.picture.contains(data)) {
                return true
            }
            mNGDetailData.picture.add(data)
            mSP.edit()
                    .putString(KEY_FAVORITE_NG_DETAIL_DATA, NGRumtime.gson.toJson(mNGDetailData.picture))
                    .apply()
        } catch (e: Exception) {
            Timber.e(e)
            mNGDetailData.picture.remove(data)
            return false
        }
        val broadcast = LocalBroadcastManager.getInstance(ctx)
        broadcast.sendBroadcast(Intent(ACTION_NG_DETAIL_DATA_CHANGED))
        return true
    }

    fun removeNGDetailDataToFavorite(data: NGDetailPictureData): Boolean {
        try {
            if (!mNGDetailData.picture.contains(data)) {
                return true
            }
            mNGDetailData.picture.remove(data)
            mSP.edit()
                    .putString(KEY_FAVORITE_NG_DETAIL_DATA, NGRumtime.gson.toJson(mNGDetailData.picture))
                    .apply()
        } catch (e: Exception) {
            Timber.e(e)
            mNGDetailData.picture.add(data)
            return false
        }
        val broadcast = LocalBroadcastManager.getInstance(ctx)
        broadcast.sendBroadcast(Intent(ACTION_NG_DETAIL_DATA_CHANGED))
        return true
    }

    fun getNGDetailData(): NGDetailData {
        mNGDetailData.picture.forEach {
            it.clearLocale()
        }
        return mNGDetailData.copy(picture = mNGDetailData.picture.toMutableList())
    }

    private fun getLastCoverUrl(): String {
        return if (mNGDetailData.picture.size > 0)
            mNGDetailData.picture.last().url else ""
    }

    private fun getImageCount() = mNGDetailData.picture.size

    fun syncFavoriteState(data: NGDetailData) {
        val favoriteIdSet = mutableSetOf<String>()
        mNGDetailData.picture.forEach {
            favoriteIdSet.add(it.id)
        }
        data.picture.forEach {
            it.favorite = favoriteIdSet.contains(it.id)
        }
    }

    fun getFavoriteAlbumData(): SelectDateAlbumData {
        return SelectDateAlbumData(
                "unset",
                String.format(Locale.getDefault(),
                        NGRumtime.application.getString(R.string.text_favorite_item_title),
                        getImageCount()),
                getLastCoverUrl(), "unset", "unset", "unset", "unset", "unset", "unset",
                "unset", "unset", "unset")
    }
}