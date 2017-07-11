package geographic.boger.me.nationalgeographic.main.selectdate

/**
 * Created by BogerChan on 2017/6/27.
 */
interface ISelectDatePresenter {
    fun init(ui: ISelectDateUI)

    fun notifyFavoriteNGDetailDataChanged()

    fun destroy()
}