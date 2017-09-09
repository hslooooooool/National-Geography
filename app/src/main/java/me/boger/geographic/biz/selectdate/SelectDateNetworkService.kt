package me.boger.geographic.biz.selectdate

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by BogerChan on 2017/6/30.
 */
interface SelectDateNetworkService {
    @GET("jiekou/mains/p{page}.html")
    fun requestNGDateData(@Path("page") page: Int): Observable<SelectDateData>
}