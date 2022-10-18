/*
 * Copyright (C) 2017 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jaredrummler.android.colorpicker

import android.graphics.*
import android.graphics.drawable.Drawable
import kotlin.math.ceil

internal class AlphaPatternDrawable(private val rectangleSize: Int = 10) : Drawable() {
    private val paint = Paint()
    private val paintWhite = Paint().apply {
        color = -0x1
    }
    private val paintGray = Paint().apply {
        color = -0x343435
    }
    private var numRectanglesHorizontal = 0
    private var numRectanglesVertical = 0

    private var bitmap: Bitmap? = null

    override fun draw(canvas: Canvas) {
        bitmap?.let { b ->
            if (!b.isRecycled) {
                canvas.drawBitmap(b, null, bounds, paint)
            }
        }
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    override fun setAlpha(alpha: Int) {
        throw UnsupportedOperationException("Alpha is not supported by this drawable.")
    }

    override fun setColorFilter(cf: ColorFilter?) {
        throw UnsupportedOperationException("ColorFilter is not supported by this drawable.")
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        val height = bounds.height()
        val width = bounds.width()
        numRectanglesHorizontal = ceil((width / rectangleSize).toDouble()).toInt()
        numRectanglesVertical = ceil((height / rectangleSize).toDouble()).toInt()
        generatePatternBitmap()
    }

    private fun generatePatternBitmap() {
        if (bounds.width() <= 0 || bounds.height() <= 0) {
            return
        }
        bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888).also {
            val canvas = Canvas(it)
            val r = Rect()
            var verticalStartWhite = true
            for (i in 0..numRectanglesVertical) {
                var isWhite = verticalStartWhite
                for (j in 0..numRectanglesHorizontal) {
                    r.top = i * rectangleSize
                    r.left = j * rectangleSize
                    r.bottom = r.top + rectangleSize
                    r.right = r.left + rectangleSize
                    canvas.drawRect(r, if (isWhite) paintWhite else paintGray)
                    isWhite = !isWhite
                }
                verticalStartWhite = !verticalStartWhite
            }
        }
    }
}