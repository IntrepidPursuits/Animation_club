package io.intrepid.socialtunes;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Drawable drawable = getResources().getDrawable(R.drawable.big_pile_of_leaves_blur, getTheme());
        if (drawable != null) {
            drawable.setAlpha(160);
            getWindow().setBackgroundDrawable(drawable);
        }
    }
}
