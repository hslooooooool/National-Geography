package geographic.boger.me.nationalgeographic.main.ngdetail

/**
 * Created by BogerChan on 2017/6/30.
 */
interface INGDetailPresenter {
    fun init(ui: INGDetailUI)

    fun shareNGDetailImage(url: String)

    fun saveNGDetailImage(url: String)

    fun setNGDetailItemFavoriteState(data: NGDetailPictureData)

    fun destroy()
}