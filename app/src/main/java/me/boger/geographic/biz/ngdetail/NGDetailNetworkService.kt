package me.boger.geographic.biz.selectdate

import me.boger.geographic.biz.ngdetail.NGDetailData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by BogerChan on 2017/6/30.
 */
interface NGDetailNetworkService {
    @GET("jiekou/albums/a{id}.html")
    fun requestNGDetailData(@Path("id") id: String): Observable<NGDetailData>
}