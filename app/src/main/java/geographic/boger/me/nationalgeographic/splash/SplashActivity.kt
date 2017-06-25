package geographic.boger.me.nationalgeographic.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jaredrummler.android.widget.AnimatedSvgView
import geographic.boger.me.nationalgeographic.R

class SplashActivity : AppCompatActivity() {

    private val asv: AnimatedSvgView by lazy { findViewById(R.id.asv_splash_logo) as AnimatedSvgView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        asv.start()
    }
}
