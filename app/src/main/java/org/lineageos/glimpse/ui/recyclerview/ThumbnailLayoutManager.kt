/*
 * SPDX-FileCopyrightText: 2023-2026 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.glimpse.ui.recyclerview

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import org.lineageos.glimpse.ext.px
import org.lineageos.glimpse.viewmodels.AlbumViewModel

class ThumbnailLayoutManager(
    private val context: Context,
    adapter: RecyclerView.Adapter<*>,
    initialSpanCount: Int = 4 // Make this a parameter with a default
) : DisplayAwareGridLayoutManager(context, initialSpanCount, 4.px) {

    init {
        spanSizeLookup = ThumbnailSpanSizeLookup(adapter, spanCount)
    }

    // Update the span count dynamically
    fun updateTargetSpanCount(newTargetSpanCount: Int, adapter: RecyclerView.Adapter<*>) {
        // Explicitly call the companion object function
        val newSpanCount = DisplayAwareGridLayoutManager.getSpanCount(context, newTargetSpanCount, 4.px)

        if (this.spanCount != newSpanCount) {
            this.spanCount = newSpanCount
            this.spanSizeLookup = ThumbnailSpanSizeLookup(adapter, newSpanCount)
        }
    }

    private class ThumbnailSpanSizeLookup(
        private val adapter: RecyclerView.Adapter<*>,
        private val spanCount: Int,
    ) : SpanSizeLookup() {
        override fun getSpanSize(position: Int) = when (adapter.getItemViewType(position)) {
            AlbumViewModel.AlbumContent.ViewType.THUMBNAIL.ordinal -> 1
            AlbumViewModel.AlbumContent.ViewType.DATE_HEADER.ordinal -> spanCount
            else -> throw Exception("Unknown view type")
        }
    }
}
