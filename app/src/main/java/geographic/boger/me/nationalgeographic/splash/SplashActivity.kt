package geographic.boger.me.nationalgeographic.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import com.facebook.drawee.view.SimpleDraweeView
import com.jaredrummler.android.widget.AnimatedSvgView
import geographic.boger.me.nationalgeographic.R
import geographic.boger.me.nationalgeographic.core.NGActivity
import geographic.boger.me.nationalgeographic.main.MainActivity

class SplashActivity : NGActivity() {

    private val asvLogo: AnimatedSvgView by lazy { findViewById(R.id.asv_splash_logo) as AnimatedSvgView }
    private val sdvSplash: SimpleDraweeView by lazy { findViewById(R.id.sdv_splash) as SimpleDraweeView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
        val ani = ScaleAnimation(1f, 1.1f, 1f, 1.1f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f)
        ani.interpolator = LinearInterpolator()
        ani.duration = 3000
        ani.fillAfter = true
        ani.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                handleJump()
            }

            override fun onAnimationStart(animation: Animation?) {
                asvLogo.start()
            }

        })
        sdvSplash.startAnimation(ani)
    }

    private fun handleJump() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
    }
}
