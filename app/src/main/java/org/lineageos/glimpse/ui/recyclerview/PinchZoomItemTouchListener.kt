/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.glimpse.ui.recyclerview

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.recyclerview.widget.RecyclerView

class PinchZoomItemTouchListener(
    context: Context,
    private val onZoomIn: () -> Unit,
    private val onZoomOut: () -> Unit
) : RecyclerView.OnItemTouchListener {

    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var scaleFactor = 1.0f

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor

            // Threshold for zooming IN (fingers moving apart, fewer columns)
            if (scaleFactor > 1.2f) {
                onZoomIn()
                scaleFactor = 1.0f // reset after triggering
                return true
            }
            // Threshold for zooming OUT (fingers moving together, more columns)
            else if (scaleFactor < 0.8f) {
                onZoomOut()
                scaleFactor = 1.0f // reset after triggering
                return true
            }
            return false
        }
    })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(e)
        // Only steal the touch event from the RecyclerView if a pinch is actively happening
        return scaleGestureDetector.isInProgress
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        scaleGestureDetector.onTouchEvent(e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}
