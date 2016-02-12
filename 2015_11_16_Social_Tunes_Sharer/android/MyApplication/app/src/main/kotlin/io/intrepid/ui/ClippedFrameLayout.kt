package io.intrepid.socialtunes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Region
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout

import io.intrepid.socialtunes.R

/**
 * Clips the children into a rounded rectangle.

 * Created by aspaans on 11/11/15.
 */
class ClippedFrameLayout : FrameLayout {

    private var radius = 0f
    private val clipPath = Path()
    private var foreGroundDrawable: Drawable? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @SuppressWarnings("unused")
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        radius = context.resources.getDimensionPixelSize(R.dimen.radius).toFloat()
        foreGroundDrawable = foreground
        foreground = null
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        clipPath.reset()
        clipPath.addRoundRect(0f, 0f, (right - left).toFloat(), (bottom - top).toFloat(), radius, radius, Path.Direction.CW)
        foreGroundDrawable?.setBounds(0, 0, right - left, bottom - top)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (foreGroundDrawable != null) {
            val rp = canvas.save(Canvas.CLIP_SAVE_FLAG)
            canvas.clipPath(clipPath, Region.Op.DIFFERENCE)

            foreGroundDrawable?.draw(canvas)

            canvas.restoreToCount(rp)
        }
    }
}
