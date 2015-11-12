package io.intrepid.socialtunes

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.support.annotation.IdRes
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
    private var buttonList: List<View?>? = null

    private var clickedButton: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shareButton = view.findViewById(android.R.id.button1)
        sharingButtons = view.findViewById(R.id.sharing_buttons)

        fun v(@IdRes id: Int): View? = view.findViewById(id)
        buttonList = listOf(v(R.id.button1), v(R.id.button2), v(R.id.button3), null)

        shareButton?.setOnClickListener {
            val width = (shareButton?.width ?: 0).toFloat()

            // Animate 'SHARE' button to the left.
            shareButton?.animate()?.setStartDelay(0)?.translationX(-width)

            // First move the 3 buttons to the right of the right border now and make them visible.
            sharingButtons?.translationX = width
            sharingButtons?.visibility = View.VISIBLE
            // Then animate them to the left, back to their original position.
            sharingButtons?.animate()?.translationX(0f)
        }

        val tmpCoord = IntArray(2)
        val tmpRect = Rect()
        val isInView = { view: View?, x: Int, y: Int ->
            if (view == null) {
                true
            }
            else {
                view.getLocationOnScreen(tmpCoord)

                tmpRect.set(tmpCoord[0], tmpCoord[1], tmpCoord[0] + view.width, tmpCoord[1] + view.height)
                tmpRect.contains(x, y)
            }
        }

        sharingButtons?.setOnTouchListener { v, event ->
            val action = event.action

            if ((action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_UP)) {
                v.getLocationOnScreen(tmpCoord)

                val clickX = tmpCoord[0] + Math.round(event.x)
                val clickY = tmpCoord[1] + Math.round(event.y)

                val targetButton = buttonList?.filter {
                    isInView(it, clickX, clickY)
                }?.get(0)

                if (action == MotionEvent.ACTION_UP) {
                    clickedButton = targetButton
                }

                // Show ripple/click only if we found a targetButton, i.e. if the user clicked on top of one of the three sharing buttons.
                // Tell the system that we didn't handle the event and it will animate a ripple (return false).
                // If not found, block the ripple animation by telling the system that we handled the touch-event (return true).
                targetButton == null
            }
            else {
                false
            }
        }

        sharingButtons?.setOnClickListener {
            if (clickedButton != null) {
                // If we clicked a sharing-button,
                // animate the 'SHARE' button back to its original position.
                // After the animation ends, do the actual click (show Toast)
                // and hide the three sharing buttons.
                //
                // Delay the animation by some interval (400ms) so that the ripple can be seen.
                shareButton!!.animate()
                    .setStartDelay(400)
                    .translationX(0f)
                    .withEndAction {
                        if (clickedButton != null) {
                            clickedButton?.performClick()
                            Toast.makeText(activity, clickedButton?.contentDescription, Toast.LENGTH_SHORT).show()
                            clickedButton = null

                            sharingButtons?.visibility = View.GONE
                        }
                    }
            }
        }
    }
}
