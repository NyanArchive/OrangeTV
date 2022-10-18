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
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import tv.orange.core.ResourceManager
import tv.orange.core.util.ViewUtil.getView
import tv.orange.core.util.ViewUtil.inflate

class ColorPaletteAdapter(
    val listener: OnColorSelectedListener,
    val colors: IntArray,
    var selectedPosition: Int
) : BaseAdapter() {
    override fun getCount(): Int {
        return colors.size
    }

    override fun getItem(position: Int): Any {
        return colors[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val vh = convertView?.let { view -> view.tag as ViewHolder } ?: ViewHolder(parent.context)

        return vh.let { holder ->
            holder.setup(position)
            holder.view
        }
    }

    fun selectNone() {
        selectedPosition = -1
        notifyDataSetChanged()
    }

    interface OnColorSelectedListener {
        fun onColorSelected(color: Int)
    }

    private inner class ViewHolder constructor(context: Context) {
        val view = context.inflate("orangetv_cpv_color_item_circle").apply {
            this.tag = this@ViewHolder
        }
        val colorPanelView: ColorPanelView = view.getView("cpv_color_panel_view")
        val imageView: ImageView = view.getView("cpv_color_image_view")

        val originalBorderColor = colorPanelView.getBorderColor()

        fun setup(position: Int) {
            val color = colors[position]
            val alpha = Color.alpha(color)
            colorPanelView.setColor(color)
            imageView.setImageResource(
                if (selectedPosition == position) {
                    ResourceManager.get().getDrawableId("orangetv_cpv_preset_checked")
                } else {
                    0
                }
            )
            if (alpha != 255) {
                if (alpha <= ColorPickerDialog.ALPHA_THRESHOLD) {
                    colorPanelView.setBorderColor(color or -0x1000000)
                    imageView.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
                } else {
                    colorPanelView.setBorderColor(originalBorderColor)
                    imageView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
                }
            } else {
                setColorFilter(position)
            }
            setOnClickListener(position)
        }

        private fun setOnClickListener(position: Int) {
            colorPanelView.setOnClickListener {
                if (selectedPosition != position) {
                    selectedPosition = position
                    notifyDataSetChanged()
                }
                listener.onColorSelected(colors[position])
            }
            colorPanelView.setOnLongClickListener {
                colorPanelView.showHint()
                true
            }
        }

        private fun setColorFilter(position: Int) {
            if (position == selectedPosition && ColorUtils.calculateLuminance(colors[position]) >= 0.65) {
                imageView.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
            } else {
                imageView.colorFilter = null
            }
        }
    }
}