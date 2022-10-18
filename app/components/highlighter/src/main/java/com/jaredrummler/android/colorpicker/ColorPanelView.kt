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
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import com.jaredrummler.android.colorpicker.DrawingUtils.dpToPx
import tv.orange.core.ResourceManager

class ColorPanelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private val alphaPaint: Paint
    private val borderPaint: Paint
    private val colorPaint: Paint
    private val borderWidthPx: Int

    private var borderColor = DEFAULT_BORDER_COLOR
    private var color = Color.BLACK

    public override fun onSaveInstanceState(): Parcelable {
        val state = Bundle()
        state.putParcelable("instanceState", super.onSaveInstanceState())
        state.putInt("color", color)
        return state
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            color = state.getInt("color")
            super.onRestoreInstanceState(state.getParcelable("instanceState"))
            return
        }

        super.onRestoreInstanceState(state)
    }

    override fun onDraw(canvas: Canvas) {
        borderPaint.color = borderColor
        colorPaint.color = color
        val outerRadius = measuredWidth / 2
        if (borderWidthPx > 0) {
            canvas.drawCircle(
                (measuredWidth / 2).toFloat(),
                (measuredHeight / 2).toFloat(),
                outerRadius.toFloat(),
                borderPaint
            )
        }
        if (Color.alpha(color) < 255) {
            canvas.drawCircle(
                (measuredWidth / 2).toFloat(),
                (measuredHeight / 2).toFloat(),
                (outerRadius - borderWidthPx).toFloat(),
                alphaPaint
            )
        }
        canvas.drawCircle(
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 2).toFloat(),
            (outerRadius - borderWidthPx).toFloat(),
            colorPaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }

    fun getColor(): Int {
        return color
    }

    fun setColor(color: Int) {
        this.color = color
        invalidate()
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderColor(color: Int) {
        borderColor = color
        invalidate()
    }

    fun showHint() {
        val screenPos = IntArray(2)
        val displayFrame = Rect()
        getLocationOnScreen(screenPos)
        getWindowVisibleDisplayFrame(displayFrame)
        val context = context
        val width = width
        val height = height
        val midy = screenPos[1] + height / 2
        var referenceX = screenPos[0] + width / 2
        if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR) {
            val screenWidth = context.resources.displayMetrics.widthPixels
            referenceX = screenWidth - referenceX
        }
        val hint = StringBuilder("#")
        if (Color.alpha(color) != 255) {
            hint.append(Integer.toHexString(color).uppercase())
        } else {
            hint.append(String.format("%06X", 0xFFFFFF and color).uppercase())
        }
        val cheatSheet = Toast.makeText(context, hint.toString(), Toast.LENGTH_SHORT)
        if (midy < displayFrame.height()) {
            cheatSheet.setGravity(
                Gravity.TOP or GravityCompat.END,
                referenceX,
                screenPos[1] + height - displayFrame.top
            )
        } else {
            cheatSheet.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, height)
        }
        cheatSheet.show()
    }

    companion object {
        private const val DEFAULT_BORDER_COLOR = -0x919192
    }

    init {
        borderWidthPx = dpToPx(context, 1f)
        borderPaint = Paint().apply {
            isAntiAlias = true
        }
        colorPaint = Paint().apply {
            isAntiAlias = true
        }
        alphaPaint = Paint().apply {
            isAntiAlias = true
            val bitmap = (ContextCompat.getDrawable(
                context,
                ResourceManager.get().getDrawableId("orangetv_cpv_alpha")
            ) as BitmapDrawable).bitmap
            shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        }
    }
}