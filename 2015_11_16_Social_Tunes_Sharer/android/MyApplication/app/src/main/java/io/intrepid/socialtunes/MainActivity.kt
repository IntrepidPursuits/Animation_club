package io.intrepid.socialtunes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlin.reflect.KFunction2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val drawable = resources.getDrawable(R.drawable.big_pile_of_leaves_blur, theme)
        if (drawable != null) {
            drawable.alpha = 160
            window.setBackgroundDrawable(drawable)
        }

        // Testing references to methods/functions.
        val ff = View::findViewById
        val f = fun () : KFunction2<View, Int, View?> = View::findViewById

        var b1: View? = ff(window.decorView, android.R.id.content)
        var b2: View? = f()(window.decorView, android.R.id.content)
    }
}
