package io.intrepid.socialtunes;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private View shareButton;
    private View sharingButtons;
    private View button1;
    private View button2;
    private View button3;
    private View clickedButton;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shareButton = view.findViewById(android.R.id.button1);
        sharingButtons = view.findViewById(R.id.sharing_buttons);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int width = shareButton.getWidth();

                // Animate 'SHARE' button to the left.
                shareButton.animate().setStartDelay(0).translationX(-width);

                // First move the 3 buttons to the right of the right border now and make them visible.
                sharingButtons.setTranslationX(width);
                sharingButtons.setVisibility(View.VISIBLE);
                // Then animate them to the left, back to their original position.
                sharingButtons.animate().translationX(0);
            }
        });

        sharingButtons.setOnTouchListener(new View.OnTouchListener() {
            private final int[] clickPoint = new int[2];
            private final Rect rect = new Rect();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();

                if ((action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_UP)) {
                    v.getLocationOnScreen(clickPoint);

                    int clickX = clickPoint[0] + Math.round(event.getX());
                    int clickY = clickPoint[1] + Math.round(event.getY());

                    View targetButton;
                    if (isInView(button1, clickX, clickY)) {
                        targetButton = button1;
                    } else if (isInView(button2, clickX, clickY)) {
                        targetButton = button2;
                    } else if (isInView(button3, clickX, clickY)) {
                        targetButton = button3;
                    } else {
                        targetButton = null;
                    }

                    if (action == MotionEvent.ACTION_UP) {
                        clickedButton = targetButton;
                    }

                    // Show ripple/click only if we found a targetButton, i.e. if the user clicked on top of one of the three sharing buttons.
                    // Tell the system that we didn't handle the event and it will animate a ripple (return false).
                    // If not found, block the ripple animation by telling the system that we handled the touch-event (return true).
                    return targetButton == null;
                }

                return false;
            }

            private boolean isInView(View view, int x, int y) {
                view.getLocationOnScreen(clickPoint);

                rect.set(clickPoint[0], clickPoint[1], clickPoint[0] + view.getWidth(), clickPoint[1] + view.getHeight());
                return rect.contains(x, y);
            }
        });

        sharingButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedButton != null) {
                    // If we clicked a sharing-button,
                    // animate the 'SHARE' button back to its original position.
                    // After the animation ends, do the actual click (show Toast)
                    // and make the three sharing buttons invisible.
                    //
                    // Delay the animation by some interval so that the ripple can be seen.
                    shareButton.animate()
                            .translationX(0)
                            .setStartDelay(400)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    clickedButton.performClick();
                                    Toast.makeText(getActivity(), clickedButton.getContentDescription(), Toast.LENGTH_SHORT).show();
                                    clickedButton = null;

                                    sharingButtons.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });
    }
}
