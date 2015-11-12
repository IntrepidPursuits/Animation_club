package io.intrepid.socialtunes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val drawable = resources.getDrawable(R.drawable.big_pile_of_leaves_blur, theme)
        if (drawable != null) {
            drawable.alpha = 160
            window.setBackgroundDrawable(drawable)
        }
    }
}
