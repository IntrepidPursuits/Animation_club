package io.intrepid.socialtunes.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import io.intrepid.socialtunes.R;

/**
 * Clips the children into a rounded rectangle.
 *
 * Created by aspaans on 11/11/15.
 */
public class ClippedFrameLayout extends FrameLayout {

    private float radius = 0;
    private Path clipPath = new Path();
    private Drawable foreGround;

    public ClippedFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public ClippedFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClippedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressWarnings("unused")
    public ClippedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        radius = context.getResources().getDimensionPixelSize(R.dimen.radius);
        foreGround = getForeground();
        setForeground(null);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        clipPath.reset();
        clipPath.addRoundRect(0, 0, right - left, bottom - top, radius, radius, Path.Direction.CW);
        foreGround.setBounds(0, 0, right - left, bottom - top);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (foreGround != null) {
            int rp = canvas.save(Canvas.CLIP_SAVE_FLAG);
            canvas.clipPath(clipPath, Region.Op.DIFFERENCE);

            foreGround.draw(canvas);

            canvas.restoreToCount(rp);
        }
    }
}
