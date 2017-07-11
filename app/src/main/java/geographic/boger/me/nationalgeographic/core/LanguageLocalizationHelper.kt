package geographic.boger.me.nationalgeographic.core

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.os.Build
import android.os.LocaleList
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.zqc.opencc.android.lib.ChineseConverter
import com.zqc.opencc.android.lib.ConversionType
import java.util.*

/**
 * Created by BogerChan on 2017/7/11.
 */
object LanguageLocalizationHelper {
    enum class Type {
        SIMPLIFIED_CHINESE,
        TRADITIONAL_CHINESE_HK,
        TRADITIONAL_CHINESE_TW,
    }

    private val KEY_LOCAL_LANGUAGE = "ng_local_language"

    var curType: Type = Type.SIMPLIFIED_CHINESE
        private set

    private val mTypefaceMap = mapOf(
            Pair(Type.SIMPLIFIED_CHINESE, DisplayProvider.primaryTypeface),
            Pair(Type.TRADITIONAL_CHINESE_HK, DisplayProvider.primaryTypeface),
            Pair(Type.TRADITIONAL_CHINESE_TW, DisplayProvider.primaryTypeface))

    var curTypeface = mTypefaceMap[curType]
        private set
        get() = mTypefaceMap[curType]

    private lateinit var mContext: Context

    private val mLanguageStateMapper = mapOf(
            Pair(Type.SIMPLIFIED_CHINESE, mapOf(
                    Pair(Type.TRADITIONAL_CHINESE_HK, ConversionType.S2HK),
                    Pair(Type.TRADITIONAL_CHINESE_TW, ConversionType.S2TWP))),
            Pair(Type.TRADITIONAL_CHINESE_TW, mapOf(
                    Pair(Type.SIMPLIFIED_CHINESE, ConversionType.TW2SP),
                    Pair(Type.TRADITIONAL_CHINESE_HK, ConversionType.T2HK))),
            Pair(Type.TRADITIONAL_CHINESE_HK, mapOf(
                    Pair(Type.SIMPLIFIED_CHINESE, ConversionType.HK2S),
                    Pair(Type.TRADITIONAL_CHINESE_TW, ConversionType.T2TW))))

    private lateinit var mSp: SharedPreferences

    fun init(ctx: Context) {
        mSp = ctx.getSharedPreferences(ctx.packageName, Context.MODE_PRIVATE)
        val type = mSp.getString(KEY_LOCAL_LANGUAGE, null)
        if (TextUtils.isEmpty(type)) {
            saveCurrentLocalSetting()
        } else {
            curType = Type.valueOf(type)
        }
        mContext = ctx.applicationContext
    }

    fun apply(act: Activity, type: Type) {
        curType = type
        act.recreate()
        saveCurrentLocalSetting()
    }

    private fun saveCurrentLocalSetting() {
        mSp.edit().putString(KEY_LOCAL_LANGUAGE, curType.toString()).apply()
    }

    fun createLocalChangeSupportContext(ctx: Context): Context {
        val locale = when (curType) {
            Type.SIMPLIFIED_CHINESE -> Locale.SIMPLIFIED_CHINESE
            Type.TRADITIONAL_CHINESE_TW -> Locale("zh", "TW")
            Type.TRADITIONAL_CHINESE_HK -> Locale("zh", "HK")
        }
        Locale.setDefault(locale)
        val conf = NGRumtime.application.resources.configuration
        val ret = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conf.setLocale(locale)
            val localList = LocaleList(locale)
            LocaleList.setDefault(localList)
            conf.locales = localList
            ctx.createConfigurationContext(conf)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(locale)
            ctx.createConfigurationContext(conf)
        } else {
            conf.locale = locale
            val res = ctx.applicationContext.resources
            res.updateConfiguration(conf, res.displayMetrics)
            ctx
        }
        return ContextWrapper(ret)
    }

    fun translate(from: Type, to: Type, text: String): String {
        if (from == to) {
            return text
        }
        return ChineseConverter.convert(text, mLanguageStateMapper[from]!![to], mContext)
    }

    private fun walk(view: View, func: (View) -> Unit) {
        if (view !is ViewGroup) {
            func(view)
            return
        }
        (0..view.childCount).forEach {
            walk(view.getChildAt(it), func)
        }
        func(view)
    }
}