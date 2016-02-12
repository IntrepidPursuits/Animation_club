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

private data class Coord(val x: Int, val y: Int)
private val tmpCoord = IntArray(2)
private val tmpRect = Rect()

private fun <T> View.applyScreenLocationTo(body: (xy: IntArray) -> T): T {
    getLocationOnScreen(tmpCoord)
    return body(tmpCoord)
}

private fun View.contains(coord: Coord): Boolean = applyScreenLocationTo {
    tmpRect.set(it[0], it[1], it[0] + width, it[1] + height)
    tmpRect.contains(coord.x, coord.y)
}

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
        buttonList = listOf(v(R.id.button1), v(R.id.button2), v(R.id.button3))

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

        sharingButtons?.setOnTouchListener { v, event ->
            val action = event.action

            if ((action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_UP)) {
                val coord = v.applyScreenLocationTo {
                    Coord(it[0] + Math.round(event.x), it[1] + Math.round(event.y))
                }

                val targetButton = buttonList?.firstOrNull {
                    it?.contains(coord) ?: false
                }

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
