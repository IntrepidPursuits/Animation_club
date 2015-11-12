package io.intrepid.socialtunes

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {

    private var shareButton: View? = null
    private var sharingButtons: View? = null
    private var button1: View? = null
    private var button2: View? = null
    private var button3: View? = null
    private var clickedButton: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shareButton = view.findViewById(android.R.id.button1)
        sharingButtons = view.findViewById(R.id.sharing_buttons)
        button1 = view.findViewById(R.id.button1)
        button2 = view.findViewById(R.id.button2)
        button3 = view.findViewById(R.id.button3)

        shareButton?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val width = shareButton?.width ?: 0

                // Animate 'SHARE' button to the left.
                shareButton?.animate()?.setStartDelay(0)?.translationX((-width).toFloat())

                // First move the 3 buttons to the right of the right border now and make them visible.
                sharingButtons?.translationX = width.toFloat()
                sharingButtons?.visibility = View.VISIBLE
                // Then animate them to the left, back to their original position.
                sharingButtons?.animate()?.translationX(0f)
            }
        })

        sharingButtons?.setOnTouchListener(object : View.OnTouchListener {
            private val clickPoint = IntArray(2)
            private val rect = Rect()

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val action = event.action

                if ((action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_UP)) {
                    v.getLocationOnScreen(clickPoint)

                    val clickX = clickPoint[0] + Math.round(event.x)
                    val clickY = clickPoint[1] + Math.round(event.y)

                    val targetButton: View?
                    if (isInView(button1, clickX, clickY)) {
                        targetButton = button1
                    } else if (isInView(button2, clickX, clickY)) {
                        targetButton = button2
                    } else if (isInView(button3, clickX, clickY)) {
                        targetButton = button3
                    } else {
                        targetButton = null
                    }

                    if (action == MotionEvent.ACTION_UP) {
                        clickedButton = targetButton
                    }

                    // Show ripple/click only if we found a targetButton, i.e. if the user clicked on top of one of the three sharing buttons.
                    // Tell the system that we didn't handle the event and it will animate a ripple (return false).
                    // If not found, block the ripple animation by telling the system that we handled the touch-event (return true).
                    return targetButton == null
                }

                return false
            }

            private fun isInView(view: View?, x: Int, y: Int): Boolean {
                if (view == null) {
                    return false;
                }

                view.getLocationOnScreen(clickPoint)

                rect.set(clickPoint[0], clickPoint[1], clickPoint[0] + view.width, clickPoint[1] + view.height)
                return rect.contains(x, y)
            }
        })

        sharingButtons?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (clickedButton != null) {
                    // If we clicked a sharing-button,
                    // animate the 'SHARE' button back to its original position.
                    // After the animation ends, do the actual click (show Toast)
                    // and make the three sharing buttons invisible.
                    //
                    // Delay the animation by some interval so that the ripple can be seen.
                    shareButton!!.animate().translationX(0f).setStartDelay(400).withEndAction(object : Runnable {
                        override fun run() {
                            if (clickedButton == null) {
                                return;
                            }

                            clickedButton?.performClick()
                            Toast.makeText(activity, clickedButton?.contentDescription, Toast.LENGTH_SHORT).show()
                            clickedButton = null

                            sharingButtons!!.visibility = View.GONE
                        }
                    })
                }
            }
        })
    }
}
