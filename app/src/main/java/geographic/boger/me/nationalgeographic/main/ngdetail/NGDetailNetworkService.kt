package geographic.boger.me.nationalgeographic.main.selectdate

import geographic.boger.me.nationalgeographic.main.ngdetail.NGDetailData
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