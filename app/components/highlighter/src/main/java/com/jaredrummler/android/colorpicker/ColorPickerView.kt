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

import android.content.Context
import android.graphics.*
import android.graphics.Shader.TileMode
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.jaredrummler.android.colorpicker.DrawingUtils.dpToPx
import tv.orange.core.ResourcesManagerCore
import kotlin.math.roundToInt

class ColorPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private val huePanelWidthPx = dpToPx(
        c = getContext(),
        dipValue = HUE_PANEL_WIDTH_DP.toFloat()
    )
    private val alphaPanelHeightPx = dpToPx(
        c = getContext(),
        dipValue = ALPHA_PANEL_HEIGHT_DP.toFloat()
    )
    private val panelSpacingPx: Int = dpToPx(
        c = getContext(),
        dipValue = PANEL_SPACING_DP.toFloat()
    )
    private val circleTrackerRadiusPx: Int = dpToPx(
        c = getContext(),
        dipValue = CIRCLE_TRACKER_RADIUS_DP.toFloat()
    )
    private val sliderTrackerOffsetPx: Int = dpToPx(
        c = getContext(),
        dipValue = SLIDER_TRACKER_OFFSET_DP.toFloat()
    )
    private val sliderTrackerSizePx: Int = dpToPx(
        c = getContext(),
        dipValue = SLIDER_TRACKER_SIZE_DP.toFloat()
    )

    private val alphaPaint = Paint()
    private val satValPaint = Paint()
    private val borderPaint = Paint()

    private val satValTrackerPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(context, 2f).toFloat()
        isAntiAlias = true
    }
    private val hueAlphaTrackerPaint = Paint().apply {
        color = sliderTrackerColor
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(context, 2f).toFloat()
        isAntiAlias = true
    }

    private var valShader: Shader? = null
    private var satShader: Shader? = null
    private var alphaShader: Shader? = null

    private var satValBackgroundCache: BitmapCache? = null
    private var hueBackgroundCache: BitmapCache? = null

    private var alpha = 0xff
    private var hue = 360f
    private var sat = 0f
    private var v = 0f

    private var showAlphaPanel = true

    private var sliderTrackerColor = DEFAULT_SLIDER_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR

    private var mRequiredPadding =
        resources.getDimensionPixelSize(ResourcesManagerCore.get().getDimenId("cpv_required_padding"))

    private var drawingRect: Rect = Rect()
    private var satValRect: Rect = Rect()
    private var hueRect: Rect = Rect()
    private var alphaRect: Rect = Rect()

    private var startTouchPoint: Point? = null

    private var alphaPatternDrawable: AlphaPatternDrawable? = null
    private var onColorChangedListener: OnColorChangedListener? = null

    var color: Int
        get() = Color.HSVToColor(alpha, floatArrayOf(hue, sat, v))
        set(color) {
            setColor(color, false)
        }

    public override fun onSaveInstanceState(): Parcelable {
        val state = Bundle()
        state.putParcelable("instanceState", super.onSaveInstanceState())
        state.putInt("alpha", alpha)
        state.putFloat("hue", hue)
        state.putFloat("sat", sat)
        state.putFloat("val", v)
        state.putBoolean("show_alpha", showAlphaPanel)
        return state
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            alpha = state.getInt("alpha")
            hue = state.getFloat("hue")
            sat = state.getFloat("sat")
            v = state.getFloat("val")
            showAlphaPanel = state.getBoolean("show_alpha")
            super.onRestoreInstanceState(state.getParcelable("instanceState"))
            return
        }
        super.onRestoreInstanceState(state)
    }

    override fun onDraw(canvas: Canvas) {
        if (drawingRect.width() <= 0 || drawingRect.height() <= 0) {
            return
        }
        drawSatValPanel(canvas)
        drawHuePanel(canvas)
        drawAlphaPanel(canvas)
    }

    private fun drawSatValPanel(canvas: Canvas) {
        val svRect = satValRect

        if (BORDER_WIDTH_PX > 0) {
            borderPaint.color = borderColor
            drawingRect.let { dRect ->
                canvas.drawRect(
                    dRect.left.toFloat(),
                    dRect.top.toFloat(),
                    (svRect.right + BORDER_WIDTH_PX).toFloat(),
                    (svRect.bottom + BORDER_WIDTH_PX).toFloat(),
                    borderPaint
                )
            }
        }

        valShader = valShader ?: LinearGradient(
            svRect.left.toFloat(),
            svRect.top.toFloat(),
            svRect.left.toFloat(),
            svRect.bottom.toFloat(),
            -0x1,
            -0x1000000,
            TileMode.CLAMP
        )

        val svCache = if (satValBackgroundCache?.value == hue) {
            satValBackgroundCache ?: createSvCache()
        } else {
            createSvCache()
        }

        canvas.drawBitmap(svCache.bitmap, null, svRect, null)
        val p = satValToPoint(sat, v)
        satValTrackerPaint.color = -0x1000000
        canvas.drawCircle(
            p.x.toFloat(),
            p.y.toFloat(),
            (circleTrackerRadiusPx - dpToPx(context, 1f)).toFloat(),
            satValTrackerPaint
        )
        satValTrackerPaint.color = -0x222223
        canvas.drawCircle(
            p.x.toFloat(),
            p.y.toFloat(),
            circleTrackerRadiusPx.toFloat(),
            satValTrackerPaint
        )
        satValBackgroundCache = svCache
    }

    private fun createSvCache(): BitmapCache {
        val svRect = satValRect

        val bitmap = Bitmap.createBitmap(
            svRect.width(),
            svRect.height(),
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        val rgb = Color.HSVToColor(floatArrayOf(hue, 1f, 1f))

        val sShader = LinearGradient(
            svRect.left.toFloat(),
            svRect.top.toFloat(),
            svRect.right.toFloat(),
            svRect.top.toFloat(),
            -0x1,
            rgb,
            TileMode.CLAMP
        ).also {
            satShader = it
        }

        valShader?.let { vShader ->
            satValPaint.shader = ComposeShader(vShader, sShader, PorterDuff.Mode.MULTIPLY)
        }

        canvas.drawRect(
            0f, 0f, bitmap.width.toFloat(),
            bitmap.height.toFloat(), satValPaint
        )

        return BitmapCache(canvas = canvas, bitmap = bitmap, value = hue)
    }

    private fun drawHuePanel(canvas: Canvas) {
        val rect = hueRect
        if (BORDER_WIDTH_PX > 0) {
            borderPaint.color = borderColor
            canvas.drawRect(
                (rect.left - BORDER_WIDTH_PX).toFloat(),
                (rect.top - BORDER_WIDTH_PX).toFloat(),
                (rect.right + BORDER_WIDTH_PX).toFloat(),
                (rect.bottom + BORDER_WIDTH_PX).toFloat(),
                borderPaint
            )
        }
        val hbCache = hueBackgroundCache ?: createHbCache()
        canvas.drawBitmap(hbCache.bitmap, null, rect, null)
        val p = hueToPoint(hue)
        val r = RectF()
        r.left = (rect.left - sliderTrackerOffsetPx).toFloat()
        r.right = (rect.right + sliderTrackerOffsetPx).toFloat()
        r.top = (p.y - sliderTrackerSizePx / 2).toFloat()
        r.bottom = (p.y + sliderTrackerSizePx / 2).toFloat()
        canvas.drawRoundRect(r, 2f, 2f, hueAlphaTrackerPaint)
        hueBackgroundCache = hbCache
    }

    private fun createHbCache(): BitmapCache {
        val hRect = hueRect

        val bitmap = Bitmap.createBitmap(hRect.width(), hRect.height(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val hueColors = IntArray((hRect.height() + 0.5f).toInt())

        var h = 360f
        for (i in hueColors.indices) {
            hueColors[i] = Color.HSVToColor(floatArrayOf(h, 1f, 1f))
            h -= 360f / hueColors.size
        }

        val linePaint = Paint()
        linePaint.strokeWidth = 0f
        for (i in hueColors.indices) {
            linePaint.color = hueColors[i]
            canvas.drawLine(
                0f,
                i.toFloat(),
                bitmap.width.toFloat(),
                i.toFloat(),
                linePaint
            )
        }

        return BitmapCache(canvas = canvas, bitmap = bitmap)
    }

    private fun drawAlphaPanel(canvas: Canvas) {
        if (!showAlphaPanel || alphaPatternDrawable == null) return
        val rect: Rect = alphaRect
        if (BORDER_WIDTH_PX > 0) {
            borderPaint.color = borderColor
            canvas.drawRect(
                (rect.left - BORDER_WIDTH_PX).toFloat(),
                (rect.top - BORDER_WIDTH_PX).toFloat(),
                (rect.right + BORDER_WIDTH_PX).toFloat(),
                (rect.bottom + BORDER_WIDTH_PX).toFloat(),
                borderPaint
            )
        }
        alphaPatternDrawable!!.draw(canvas)
        val hsv = floatArrayOf(hue, sat, v)
        val color = Color.HSVToColor(hsv)
        val acolor = Color.HSVToColor(0, hsv)
        alphaShader =
            LinearGradient(
                rect.left.toFloat(),
                rect.top.toFloat(),
                rect.right.toFloat(),
                rect.top.toFloat(),
                color,
                acolor,
                TileMode.CLAMP
            )
        alphaPaint.shader = alphaShader
        canvas.drawRect(rect, alphaPaint)
        val p = alphaToPoint(alpha)
        val r = RectF()
        r.left = (p.x - sliderTrackerSizePx / 2).toFloat()
        r.right = (p.x + sliderTrackerSizePx / 2).toFloat()
        r.top = (rect.top - sliderTrackerOffsetPx).toFloat()
        r.bottom = (rect.bottom + sliderTrackerOffsetPx).toFloat()
        canvas.drawRoundRect(r, 2f, 2f, hueAlphaTrackerPaint)
    }

    private fun hueToPoint(hue: Float): Point {
        val rect = hueRect
        val height = rect.height().toFloat()
        val p = Point()
        p.y = (height - hue * height / 360f + rect.top).toInt()
        p.x = rect.left
        return p
    }

    private fun satValToPoint(sat: Float, v: Float): Point {
        val rect = satValRect
        val height = rect.height().toFloat()
        val width = rect.width().toFloat()
        val p = Point()
        p.x = (sat * width + rect.left).toInt()
        p.y = ((1f - v) * height + rect.top).toInt()
        return p
    }

    private fun alphaToPoint(alpha: Int): Point {
        val rect = alphaRect
        val width = rect.width().toFloat()
        val p = Point()
        p.x = (width - alpha * width / 0xff + rect.left).toInt()
        p.y = rect.top
        return p
    }

    private fun pointToSatVal(x: Float, y: Float): FloatArray {
        var xTemp = x
        var yTemp = y
        val rect = satValRect
        val result = FloatArray(2)
        val width = rect.width().toFloat()
        val height = rect.height().toFloat()
        xTemp = if (xTemp < rect.left) {
            0f
        } else if (xTemp > rect.right) {
            width
        } else {
            xTemp - rect.left
        }
        yTemp = if (yTemp < rect.top) {
            0f
        } else if (yTemp > rect.bottom) {
            height
        } else {
            yTemp - rect.top
        }
        result[0] = 1f / width * xTemp
        result[1] = 1f - 1f / height * yTemp
        return result
    }

    private fun pointToHue(y: Float): Float {
        var y = y
        val rect = hueRect
        val height = rect.height().toFloat()
        y = if (y < rect.top) {
            0f
        } else if (y > rect.bottom) {
            height
        } else {
            y - rect.top
        }
        return 360f - y * 360f / height
    }

    private fun pointToAlpha(x: Int): Int {
        var xTemp = x
        val rect = alphaRect
        val width = rect.width()
        xTemp = if (xTemp < rect.left) {
            0
        } else if (xTemp > rect.right) {
            width
        } else {
            xTemp - rect.left
        }
        return 0xff - xTemp * 0xff / width
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var update = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTouchPoint = Point(event.x.toInt(), event.y.toInt())
                update = moveTrackersIfNeeded(event)
            }
            MotionEvent.ACTION_MOVE -> update = moveTrackersIfNeeded(event)
            MotionEvent.ACTION_UP -> {
                startTouchPoint = null
                update = moveTrackersIfNeeded(event)
            }
        }
        if (update) {
            if (onColorChangedListener != null) {
                onColorChangedListener!!.onColorChanged(
                    Color.HSVToColor(
                        alpha,
                        floatArrayOf(hue, sat, v)
                    )
                )
            }
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun moveTrackersIfNeeded(event: MotionEvent): Boolean {
        if (startTouchPoint == null) {
            return false
        }
        var update = false
        val startX = startTouchPoint!!.x
        val startY = startTouchPoint!!.y
        if (hueRect.contains(startX, startY)) {
            hue = pointToHue(event.y)
            update = true
        } else if (satValRect.contains(startX, startY)) {
            val result = pointToSatVal(event.x, event.y)
            sat = result[0]
            v = result[1]
            update = true
        } else if (alphaRect.contains(startX, startY)) {
            alpha = pointToAlpha(event.x.toInt())
            update = true
        }
        return update
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val finalWidth: Int
        val finalHeight: Int
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthAllowed = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val heightAllowed = MeasureSpec.getSize(heightMeasureSpec) - paddingBottom - paddingTop
        if (widthMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.EXACTLY) {
            if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
                var h = widthAllowed - panelSpacingPx - huePanelWidthPx
                if (showAlphaPanel) {
                    h += panelSpacingPx + alphaPanelHeightPx
                }
                finalHeight = if (h > heightAllowed) {
                    heightAllowed
                } else {
                    h
                }
                finalWidth = widthAllowed
            } else if (heightMode == MeasureSpec.EXACTLY && widthMode != MeasureSpec.EXACTLY) {
                var w = heightAllowed + panelSpacingPx + huePanelWidthPx
                if (showAlphaPanel) {
                    w -= panelSpacingPx + alphaPanelHeightPx
                }
                finalWidth = if (w > widthAllowed) {
                    widthAllowed
                } else {
                    w
                }
                finalHeight = heightAllowed
            } else {
                finalWidth = widthAllowed
                finalHeight = heightAllowed
            }
        } else {
            var widthNeeded = heightAllowed + panelSpacingPx + huePanelWidthPx

            var heightNeeded = widthAllowed - panelSpacingPx - huePanelWidthPx
            if (showAlphaPanel) {
                widthNeeded -= panelSpacingPx + alphaPanelHeightPx
                heightNeeded += panelSpacingPx + alphaPanelHeightPx
            }
            var widthOk = false
            var heightOk = false
            if (widthNeeded <= widthAllowed) {
                widthOk = true
            }
            if (heightNeeded <= heightAllowed) {
                heightOk = true
            }
            if (widthOk && heightOk) {
                finalWidth = widthAllowed
                finalHeight = heightNeeded
            } else if (!heightOk && widthOk) {
                finalHeight = heightAllowed
                finalWidth = widthNeeded
            } else if (!widthOk && heightOk) {
                finalHeight = heightNeeded
                finalWidth = widthAllowed
            } else {
                finalHeight = heightAllowed
                finalWidth = widthAllowed
            }
        }
        setMeasuredDimension(
            finalWidth + paddingLeft + paddingRight,
            finalHeight + paddingTop + paddingBottom
        )
    }

    override fun getPaddingTop(): Int {
        return super.getPaddingTop().coerceAtLeast(mRequiredPadding)
    }

    override fun getPaddingBottom(): Int {
        return super.getPaddingBottom().coerceAtLeast(mRequiredPadding)
    }

    override fun getPaddingLeft(): Int {
        return super.getPaddingLeft().coerceAtLeast(mRequiredPadding)
    }

    override fun getPaddingRight(): Int {
        return super.getPaddingRight().coerceAtLeast(mRequiredPadding)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingRect = Rect()
        drawingRect.left = paddingLeft
        drawingRect.right = w - paddingRight
        drawingRect.top = paddingTop
        drawingRect.bottom = h - paddingBottom

        valShader = null
        satShader = null
        alphaShader = null

        satValBackgroundCache = null
        hueBackgroundCache = null
        setUpSatValRect()
        setUpHueRect()
        setUpAlphaRect()
    }

    private fun setUpSatValRect() {
        drawingRect.let { rect ->
            val left = rect.left + BORDER_WIDTH_PX
            val top = rect.top + BORDER_WIDTH_PX
            var bottom = rect.bottom - BORDER_WIDTH_PX
            val right = rect.right - BORDER_WIDTH_PX - panelSpacingPx - huePanelWidthPx
            if (showAlphaPanel) {
                bottom -= alphaPanelHeightPx + panelSpacingPx
            }
            satValRect = Rect(left, top, right, bottom)
        }
    }

    private fun setUpHueRect() {
        val dRect = drawingRect
        val left = dRect.right - huePanelWidthPx + BORDER_WIDTH_PX
        val top = dRect.top + BORDER_WIDTH_PX
        val bottom = dRect.bottom - BORDER_WIDTH_PX - if (showAlphaPanel) {
            panelSpacingPx + alphaPanelHeightPx
        } else {
            0
        }
        val right = dRect.right - BORDER_WIDTH_PX
        hueRect = Rect(left, top, right, bottom)
    }

    private fun setUpAlphaRect() {
        if (!showAlphaPanel) return
        val left = drawingRect.left + BORDER_WIDTH_PX
        val top = drawingRect.bottom - alphaPanelHeightPx + BORDER_WIDTH_PX
        val bottom = drawingRect.bottom - BORDER_WIDTH_PX
        val right = drawingRect.right - BORDER_WIDTH_PX
        alphaRect = Rect(left, top, right, bottom)
        alphaPatternDrawable = AlphaPatternDrawable(dpToPx(context, 4f)).apply {
            setBounds(
                alphaRect.left.toFloat().roundToInt(),
                alphaRect.top.toFloat().roundToInt(),
                alphaRect.right.toFloat().roundToInt(),
                alphaRect.bottom.toFloat().roundToInt()
            )
        }
    }

    fun setOnColorChangedListener(listener: OnColorChangedListener?) {
        onColorChangedListener = listener
    }

    fun setColor(color: Int, callback: Boolean) {
        val alpha = Color.alpha(color)
        val red = Color.red(color)
        val blue = Color.blue(color)
        val green = Color.green(color)
        val hsv = FloatArray(3)
        Color.RGBToHSV(red, green, blue, hsv)
        this.alpha = alpha
        hue = hsv[0]
        sat = hsv[1]
        v = hsv[2]
        if (callback) {
            onColorChangedListener?.onColorChanged(
                Color.HSVToColor(
                    this.alpha,
                    floatArrayOf(hue, sat, v)
                )
            )
        }
        invalidate()
    }

    private data class BitmapCache(val canvas: Canvas, val bitmap: Bitmap, var value: Float = 0f)

    companion object {
        private const val DEFAULT_BORDER_COLOR = -0x919192
        private const val DEFAULT_SLIDER_COLOR = -0x424243
        private const val HUE_PANEL_WIDTH_DP = 30
        private const val ALPHA_PANEL_HEIGHT_DP = 20
        private const val PANEL_SPACING_DP = 10
        private const val CIRCLE_TRACKER_RADIUS_DP = 5
        private const val SLIDER_TRACKER_SIZE_DP = 4
        private const val SLIDER_TRACKER_OFFSET_DP = 2
        private const val BORDER_WIDTH_PX = 1
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }
}