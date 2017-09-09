package me.boger.geographic.biz.common

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.google.gson.reflect.TypeToken
import me.boger.geographic.R
import me.boger.geographic.core.NGRumtime
import me.boger.geographic.biz.ngdetail.NGDetailData
import me.boger.geographic.biz.ngdetail.NGDetailPictureData
import me.boger.geographic.biz.selectdate.SelectDateAlbumData
import me.boger.geographic.core.NGBroadcastManager
import me.boger.geographic.core.NGConstants
import me.boger.geographic.util.Timber
import java.util.*

/**
 * Created by BogerChan on 2017/7/10.
 */
class FavoriteNGDataSupplier(ctx: Context) {

    companion object {
        val KEY_FAVORITE_NG_DETAIL_DATA = "fav_ng_detail_data"
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
        NGBroadcastManager.sendLocalBroadcast(Intent(NGConstants.ACTION_FAVORITE_DATA_CHANGED))
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
        NGBroadcastManager.sendLocalBroadcast(Intent(NGConstants.ACTION_FAVORITE_DATA_CHANGED))
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