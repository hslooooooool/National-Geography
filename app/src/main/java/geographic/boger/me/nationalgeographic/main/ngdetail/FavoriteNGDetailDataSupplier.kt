package geographic.boger.me.nationalgeographic.main.ngdetail

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import geographic.boger.me.nationalgeographic.util.Timber

/**
 * Created by BogerChan on 2017/7/10.
 */
class FavoriteNGDetailDataSupplier(val ctx: Context) {

    companion object {
        val KEY_FAVORITE_NG_DETAIL_DATA = "fav_ng_detail_data"

        val ACTION_NG_DETAIL_DATA_CHANGED = "geographic.boger.me.nationalgeographic.ACTION_NG_DETAIL_DATA"
    }

    private var mNGDetailData: NGDetailData = NGDetailData("0", ArrayList<NGDetailPictureData>(0))

    private val mGson = Gson()

    private var mSP = ctx.getSharedPreferences(ctx.packageName, Context.MODE_PRIVATE)

    init {
        val jsonNGDetailData = mSP.getString(KEY_FAVORITE_NG_DETAIL_DATA, null)
        if (!TextUtils.isEmpty(jsonNGDetailData)) {
            val list = mGson.fromJson<MutableList<NGDetailPictureData>>(
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
                    .putString(KEY_FAVORITE_NG_DETAIL_DATA, mGson.toJson(mNGDetailData.picture))
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
                    .putString(KEY_FAVORITE_NG_DETAIL_DATA, mGson.toJson(mNGDetailData.picture))
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
        return mNGDetailData.copy(picture = mNGDetailData.picture.toMutableList())
    }

    fun syncFavoriteState(data: NGDetailData) {
        val favoriteIdSet = mutableSetOf<String>()
        mNGDetailData.picture.forEach {
            favoriteIdSet.add(it.id)
        }
        data.picture.forEach {
            it.favorite = favoriteIdSet.contains(it.id)
        }
    }
}