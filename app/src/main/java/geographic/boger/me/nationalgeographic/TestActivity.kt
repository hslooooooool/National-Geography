package geographic.boger.me.nationalgeographic

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

/**
 * Created by BogerChan on 2017/6/25.
 */
class TestActivity : AppCompatActivity() {

    private val btnOpen: Button by lazy { findViewById(R.id.btn_open_dlan) as Button }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)
        btnOpen.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_WIFI_SETTINGS))
        }
    }
}