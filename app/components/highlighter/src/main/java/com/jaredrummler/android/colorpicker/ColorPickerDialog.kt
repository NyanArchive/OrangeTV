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

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.DialogFragment
import com.jaredrummler.android.colorpicker.ColorPaletteAdapter.OnColorSelectedListener
import tv.orange.core.ResourceManager
import tv.orange.core.util.ViewUtil.getView
import tv.orange.core.util.ViewUtil.inflate
import java.util.*
import kotlin.math.roundToInt

class ColorPickerDialog : DialogFragment(), OnColorChangedListener, TextWatcher {
    private val listeners = mutableListOf<ColorPickerDialogListener>()

    private lateinit var rootView: FrameLayout
    private lateinit var colorPicker: ColorPickerView
    private lateinit var newColorPanel: ColorPanelView
    private lateinit var oldColorPanel: ColorPanelView
    private lateinit var hexEditText: EditText
    private lateinit var shadesLayout: LinearLayout
    private lateinit var transparencySeekBar: SeekBar
    private lateinit var transparencyPercText: TextView
    private lateinit var adapter: ColorPaletteAdapter
    private lateinit var presets: IntArray

    private var dialogId = 0
    private var dialogType = TYPE_PRESETS

    private var showAlphaSlider = true
    private var showColorShades = true

    @ColorInt
    private var color = 0

    private var presetsButtonStringRes = 0
    private var fromEditText = false
    private var customButtonStringRes = 0

    @SuppressLint("ClickableViewAccessibility")
    private val onPickerTouchListener = OnTouchListener { v, _ ->
        if (v !== hexEditText && hexEditText.hasFocus()) {
            hexEditText.clearFocus()
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(hexEditText.windowToken, 0)
            hexEditText.clearFocus()
            return@OnTouchListener true
        }
        false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogId = requireArguments().getInt(ARG_ID)
        showAlphaSlider = requireArguments().getBoolean(ARG_ALPHA)
        showColorShades = requireArguments().getBoolean(ARG_SHOW_COLOR_SHADES)

        savedInstanceState?.let { sis ->
            color = sis.getInt(ARG_COLOR)
            dialogType = sis.getInt(ARG_TYPE)
        } ?: run {
            color = requireArguments().getInt(ARG_COLOR)
            dialogType = requireArguments().getInt(ARG_TYPE)
        }

        rootView = FrameLayout(requireActivity())

        if (dialogType == TYPE_CUSTOM) {
            rootView.addView(createPickerView())
        } else if (dialogType == TYPE_PRESETS) {
            rootView.addView(createPresetsView())
        }

        var selectedButtonStringRes = requireArguments().getInt(ARG_SELECTED_BUTTON_TEXT)
        if (selectedButtonStringRes == 0) {
            selectedButtonStringRes = ResourceManager.get().getStringId("cpv_select")
        }
        val builder = AlertDialog.Builder(requireActivity())
            .setView(rootView)
            .setPositiveButton(ResourceManager.get().getString(selectedButtonStringRes)) { _, _ ->
                onColorSelected(color)
            }

        val dialogTitleStringRes = requireArguments().getInt(ARG_DIALOG_TITLE)
        if (dialogTitleStringRes != 0) {
            builder.setTitle(dialogTitleStringRes)
        }

        presetsButtonStringRes = requireArguments().getInt(ARG_PRESETS_BUTTON_TEXT)
        customButtonStringRes = requireArguments().getInt(ARG_CUSTOM_BUTTON_TEXT)

        val neutralButtonStringRes =
            if (dialogType == TYPE_CUSTOM && requireArguments().getBoolean(ARG_ALLOW_PRESETS)) {
                if (presetsButtonStringRes != 0) {
                    presetsButtonStringRes
                } else {
                    ResourceManager.get().getStringId("cpv_presets")
                }
            } else if (dialogType == TYPE_PRESETS && requireArguments().getBoolean(ARG_ALLOW_CUSTOM)) {
                if (customButtonStringRes != 0) {
                    customButtonStringRes
                } else {
                    ResourceManager.get().getStringId("cpv_custom")
                }
            } else {
                0
            }

        if (neutralButtonStringRes != 0) {
            builder.setNeutralButton(ResourceManager.get().getString(neutralButtonStringRes), null)
        }

        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        val dialog = requireDialog() as AlertDialog

        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        val neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL)
        neutralButton?.setOnClickListener { v ->
            rootView.removeAllViews()
            when (dialogType) {
                TYPE_CUSTOM -> {
                    dialogType = TYPE_PRESETS
                    (v as Button).setText(
                        if (customButtonStringRes != 0) {
                            customButtonStringRes
                        } else {
                            ResourceManager.get().getStringId("cpv_custom")
                        }
                    )
                    rootView.addView(createPresetsView())
                }
                TYPE_PRESETS -> {
                    dialogType = TYPE_CUSTOM
                    (v as Button).setText(
                        if (presetsButtonStringRes != 0) {
                            presetsButtonStringRes
                        } else {
                            ResourceManager.get().getStringId("cpv_presets")
                        }
                    )
                    rootView.addView(createPickerView())
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismissed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ARG_COLOR, color)
        outState.putInt(ARG_TYPE, dialogType)
        super.onSaveInstanceState(outState)
    }

    fun addColorPickerDialogListener(listener: ColorPickerDialogListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun removeColorPickerDialogListener(listener: ColorPickerDialogListener) {
        listeners.remove(listener)
    }

    private fun createPickerView(): View {
        val contentView = requireContext().inflate("orangetv_cpv_dialog_color_picker")

        colorPicker = contentView.getView("cpv_color_picker_view")
        newColorPanel = contentView.getView("cpv_color_panel_new")
        oldColorPanel = contentView.getView("cpv_color_panel_old")
        hexEditText = contentView.getView("cpv_hex")

        colorPicker.setAlphaSliderVisible(showAlphaSlider)
        colorPicker.setColor(color, true)

        newColorPanel.setColor(color)
        oldColorPanel.setColor(color)

        setHex(color)

        if (!showAlphaSlider) {
            hexEditText.filters = arrayOf<InputFilter>(LengthFilter(6))
        }

        newColorPanel.setOnClickListener {
            if (newColorPanel.getColor() == color) {
                onColorSelected(color)
                dismiss()
            }
        }

        contentView.setOnTouchListener(onPickerTouchListener)
        colorPicker.setOnColorChangedListener(this)
        hexEditText.addTextChangedListener(this)
        hexEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(hexEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        return contentView
    }

    override fun onColorChanged(newColor: Int) {
        color = newColor
        newColorPanel.setColor(newColor)

        if (!fromEditText) {
            setHex(newColor)
            if (hexEditText.hasFocus()) {
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(hexEditText.windowToken, 0)
                hexEditText.clearFocus()
            }
        }
        fromEditText = false
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (hexEditText.isFocused) {
            val color = parseColorString(s.toString())
            if (color != colorPicker.color) {
                fromEditText = true
                colorPicker.setColor(color, true)
            }
        }
    }

    private fun setHex(color: Int) {
        if (showAlphaSlider) {
            hexEditText.setText(String.format("%08X", color))
        } else {
            hexEditText.setText(String.format("%06X", 0xFFFFFF and color))
        }
    }

    @Throws(NumberFormatException::class)
    private fun parseColorString(s: String): Int {
        val colorString = if (s.startsWith("#")) {
            s.substring(1).trim()
        } else {
            s.trim()
        }

        val a: Int
        var r: Int
        val g: Int
        var b = 0

        if (colorString.isEmpty()) {
            r = 0
            a = 255
            g = 0
        } else if (colorString.length <= 2) {
            a = 255
            r = 0
            b = colorString.toInt(16)
            g = 0
        } else if (colorString.length == 3) {
            a = 255
            r = colorString.substring(0, 1).toInt(16)
            g = colorString.substring(1, 2).toInt(16)
            b = colorString.substring(2, 3).toInt(16)
        } else if (colorString.length == 4) {
            a = 255
            r = colorString.substring(0, 2).toInt(16)
            g = r
            r = 0
            b = colorString.substring(2, 4).toInt(16)
        } else if (colorString.length == 5) {
            a = 255
            r = colorString.substring(0, 1).toInt(16)
            g = colorString.substring(1, 3).toInt(16)
            b = colorString.substring(3, 5).toInt(16)
        } else if (colorString.length == 6) {
            a = 255
            r = colorString.substring(0, 2).toInt(16)
            g = colorString.substring(2, 4).toInt(16)
            b = colorString.substring(4, 6).toInt(16)
        } else if (colorString.length == 7) {
            a = colorString.substring(0, 1).toInt(16)
            r = colorString.substring(1, 3).toInt(16)
            g = colorString.substring(3, 5).toInt(16)
            b = colorString.substring(5, 7).toInt(16)
        } else if (colorString.length == 8) {
            a = colorString.substring(0, 2).toInt(16)
            r = colorString.substring(2, 4).toInt(16)
            g = colorString.substring(4, 6).toInt(16)
            b = colorString.substring(6, 8).toInt(16)
        } else {
            b = -1
            g = -1
            r = -1
            a = -1
        }

        return Color.argb(a, r, g, b)
    }

    private fun createPresetsView(): View {
        val contentView = requireContext().inflate("orangetv_cpv_dialog_presets")

        shadesLayout = contentView.getView("shades_layout")
        transparencySeekBar = contentView.getView("transparency_seekbar")
        transparencyPercText = contentView.getView("transparency_text")

        val gridView = contentView.getView<GridView>("gridView")
        loadPresets()
        if (showColorShades) {
            createColorShades(color)
        } else {
            shadesLayout.visibility = View.GONE
            contentView.getView<View>("shades_divider").visibility = View.GONE
        }
        adapter = ColorPaletteAdapter(object : OnColorSelectedListener {
            override fun onColorSelected(color: Int) {
                if (this@ColorPickerDialog.color == color) {
                    this@ColorPickerDialog.onColorSelected(this@ColorPickerDialog.color)
                    dismiss()
                    return
                }
                this@ColorPickerDialog.color = color
                if (showColorShades) {
                    createColorShades(this@ColorPickerDialog.color)
                }
            }
        }, presets, selectedItemPosition)

        gridView.adapter = adapter
        if (showAlphaSlider) {
            setupTransparency()
        } else {
            contentView.getView<View>("transparency_layout").visibility = View.GONE
            contentView.getView<View>("transparency_title").visibility = View.GONE
        }

        return contentView
    }

    private fun loadPresets() {
        val alpha = Color.alpha(color)
        val res = requireArguments().getIntArray(ARG_PRESETS) ?: MATERIAL_COLORS

        presets = res.copyOf(res.size)
        if (showAlphaSlider) {
            if (alpha != 255) {
                for (i in presets.indices) {
                    val color = presets.get(i)
                    val red = Color.red(color)
                    val green = Color.green(color)
                    val blue = Color.blue(color)
                    presets[i] = Color.argb(alpha, red, green, blue)
                }
            }
        }
        presets = unshiftIfNotExists(presets, color)
        val initialColor = requireArguments().getInt(ARG_COLOR)
        if (initialColor != color) {
            presets = unshiftIfNotExists(presets, initialColor)
            color = initialColor
        }
    }

    fun createColorShades(@ColorInt color: Int) {
        val colorShades = getColorShades(color)
        if (shadesLayout.childCount != 0) {
            for (i in 0 until shadesLayout.childCount) {
                val layout = shadesLayout.getChildAt(i) as FrameLayout
                val cpv = layout.getView<ColorPanelView>("cpv_color_panel_view")
                val iv = layout.getView<ImageView>("cpv_color_image_view")

                cpv.setColor(colorShades[i])
                cpv.tag = false
                iv.setImageDrawable(null)
            }

            return
        }

        val horizontalPadding = resources.getDimensionPixelSize(
            ResourceManager.get().getDimenId("cpv_item_horizontal_padding")
        )

        for (colorShade in colorShades) {
            val view = requireContext().inflate("orangetv_cpv_color_item_circle")
            val colorPanelView = view.getView<ColorPanelView>("cpv_color_panel_view")
            val params = colorPanelView.layoutParams as MarginLayoutParams

            params.rightMargin = horizontalPadding
            params.leftMargin = params.rightMargin
            colorPanelView.layoutParams = params
            colorPanelView.setColor(colorShade)
            shadesLayout.addView(view)
            colorPanelView.post {
                colorPanelView.setColor(colorShade)
            }
            colorPanelView.setOnClickListener(View.OnClickListener { v ->
                if (v.tag is Boolean && v.tag as Boolean) {
                    onColorSelected(this@ColorPickerDialog.color)
                    dismiss()
                    return@OnClickListener
                }
                this@ColorPickerDialog.color = colorPanelView.getColor()
                adapter.selectNone()

                for (i in 0 until shadesLayout.childCount) {
                    val layout = shadesLayout.getChildAt(i) as FrameLayout
                    val cpv = layout.getView<ColorPanelView>("cpv_color_panel_view")
                    val iv = layout.getView<ImageView>("cpv_color_image_view")

                    iv.setImageResource(
                        if (cpv === v) {
                            ResourceManager.get().getDrawableId(
                                resName = "orangetv_cpv_preset_checked"
                            )
                        } else {
                            0
                        }
                    )
                    if (cpv === v && ColorUtils.calculateLuminance(cpv.getColor()) >= 0.65
                        || Color.alpha(cpv.getColor()) <= ALPHA_THRESHOLD
                    ) {
                        iv.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
                    } else {
                        iv.colorFilter = null
                    }
                    cpv.tag = cpv === v
                }
            })
            colorPanelView.setOnLongClickListener {
                colorPanelView.showHint()
                true
            }
        }
    }

    private fun onColorSelected(color: Int) {
        listeners.forEach { it.onColorSelected(dialogId, color) }
    }

    private fun onDialogDismissed() {
        listeners.forEach { it.onDialogDismissed(dialogId) }
    }

    private fun shadeColor(@ColorInt color: Int, percent: Double): Int {
        val hex = String.format("#%06X", 0xFFFFFF and color)
        val f = hex.substring(1).toLong(16)
        val t: Double = if (percent < 0) 0.0 else 255.toDouble()
        val p = if (percent < 0) percent * -1 else percent
        val R = f shr 16
        val G = f shr 8 and 0x00FF
        val B = f and 0x0000FF
        val alpha = Color.alpha(color)
        val red = (((t - R) * p).roundToInt() + R).toInt()
        val green = (((t - G) * p).roundToInt() + G).toInt()
        val blue = (((t - B) * p).roundToInt() + B).toInt()
        return Color.argb(alpha, red, green, blue)
    }

    private fun getColorShades(@ColorInt color: Int): IntArray {
        return intArrayOf(
            shadeColor(color, 0.9),
            shadeColor(color, 0.7),
            shadeColor(color, 0.5),
            shadeColor(color, 0.333),
            shadeColor(color, 0.166),
            shadeColor(color, -0.125),
            shadeColor(color, -0.25),
            shadeColor(color, -0.375),
            shadeColor(color, -0.5),
            shadeColor(color, -0.675),
            shadeColor(color, -0.7),
            shadeColor(color, -0.775)
        )
    }

    private fun setupTransparency() {
        val progress = 255 - Color.alpha(color)
        transparencySeekBar.max = 255
        transparencySeekBar.progress = progress
        val percentage = (progress.toDouble() * 100 / 255).toInt()
        transparencyPercText.text = String.format(Locale.ENGLISH, "%d%%", percentage)
        transparencySeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val perc = (progress.toDouble() * 100 / 255).toInt()
                transparencyPercText.text = String.format(Locale.ENGLISH, "%d%%", perc)
                val alpha = 255 - progress
                for (i in 0 until adapter.colors.size) {
                    val color = adapter.colors[i]
                    val red = Color.red(color)
                    val green = Color.green(color)
                    val blue = Color.blue(color)
                    adapter.colors[i] = Color.argb(alpha, red, green, blue)
                }
                adapter.notifyDataSetChanged()
                for (i in 0 until shadesLayout.childCount) {
                    val layout = shadesLayout.getChildAt(i) as FrameLayout

                    val cpv = layout.getView<ColorPanelView>("cpv_color_panel_view")
                    val iv = layout.getView<ImageView>("cpv_color_image_view")

                    if (layout.tag == null) {
                        layout.tag = cpv.getBorderColor()
                    }
                    var color = cpv.getColor()
                    color =
                        Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
                    if (alpha <= ALPHA_THRESHOLD) {
                        cpv.setBorderColor(color or -0x1000000)
                    } else {
                        cpv.setBorderColor(layout.tag as Int)
                    }
                    if (cpv.tag != null && cpv.tag as Boolean) {
                        if (alpha <= ALPHA_THRESHOLD) {
                            iv.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
                        } else {
                            if (ColorUtils.calculateLuminance(color) >= 0.65) {
                                iv.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
                            } else {
                                iv.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
                            }
                        }
                    }
                    cpv.setColor(color)
                }
                val red = Color.red(color)
                val green = Color.green(color)
                val blue = Color.blue(color)
                color = Color.argb(alpha, red, green, blue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun unshiftIfNotExists(array: IntArray, value: Int): IntArray {
        var present = false
        for (i in array) {
            if (i == value) {
                present = true
                break
            }
        }
        if (!present) {
            val newArray = IntArray(array.size + 1)
            newArray[0] = value
            System.arraycopy(array, 0, newArray, 1, newArray.size - 1)
            return newArray
        }

        return array
    }

    private fun pushIfNotExists(array: IntArray, value: Int): IntArray {
        var present = false
        for (i in array) {
            if (i == value) {
                present = true
                break
            }
        }
        if (!present) {
            val newArray = IntArray(array.size + 1)
            newArray[newArray.size - 1] = value
            System.arraycopy(array, 0, newArray, 0, newArray.size - 1)
            return newArray
        }

        return array
    }

    private val selectedItemPosition: Int
        get() {
            for (i in presets.indices) {
                if (presets[i] == color) {
                    return i
                }
            }
            return -1
        }

    class Builder internal constructor() {
        @StringRes
        var dialogTitle = ResourceManager.get().getStringId("cpv_default_title")

        @StringRes
        var presetsButtonText = ResourceManager.get().getStringId("cpv_presets")

        @StringRes
        var customButtonText = ResourceManager.get().getStringId("cpv_custom")

        @StringRes
        var selectedButtonText = ResourceManager.get().getStringId("cpv_select")

        @ColorInt
        var color = Color.BLACK

        var allowPresets = true
        var allowCustom = true

        fun setColor(@ColorInt color: Int): Builder {
            return this.apply {
                this@Builder.color = color
            }
        }

        fun create() = ColorPickerDialog().apply {
            arguments = Bundle().apply {
                putInt(ARG_ID, dialogId)
                putInt(ARG_TYPE, dialogType)
                putInt(ARG_COLOR, this@Builder.color)
                putBoolean(ARG_ALLOW_CUSTOM, this@Builder.allowCustom)
                putBoolean(ARG_ALLOW_PRESETS, this@Builder.allowPresets)
                putIntArray(ARG_PRESETS, MATERIAL_COLORS)
                putBoolean(ARG_ALPHA, showAlphaSlider)
                putInt(ARG_DIALOG_TITLE, dialogTitle)
                putBoolean(ARG_SHOW_COLOR_SHADES, showColorShades)
                putInt(ARG_PRESETS_BUTTON_TEXT, presetsButtonText)
                putInt(ARG_CUSTOM_BUTTON_TEXT, customButtonText)
                putInt(ARG_SELECTED_BUTTON_TEXT, selectedButtonText)
            }
        }
    }

    companion object {
        const val TYPE_CUSTOM = 0
        const val TYPE_PRESETS = 1

        @ColorInt
        val MATERIAL_COLORS = intArrayOf(
            -0xbbcca,  // RED 500
            -0x16e19d,  // PINK 500
            -0xd36d,  // LIGHT PINK 500
            -0x63d850,  // PURPLE 500
            -0x98c549,  // DEEP PURPLE 500
            -0xc0ae4b,  // INDIGO 500
            -0xde690d,  // BLUE 500
            -0xfc560c,  // LIGHT BLUE 500
            -0xff432c,  // CYAN 500
            -0xff6978,  // TEAL 500
            -0xb350b0,  // GREEN 500
            -0x743cb6,  // LIGHT GREEN 500
            -0x3223c7,  // LIME 500
            -0x14c5,  // YELLOW 500
            -0x3ef9,  // AMBER 500
            -0x6800,  // ORANGE 500
            -0x86aab8,  // BROWN 500
            -0x9f8275,  // BLUE GREY 500
            -0x616162
        )

        const val ALPHA_THRESHOLD = 165

        private const val ARG_ID = "id"
        private const val ARG_TYPE = "dialogType"
        private const val ARG_COLOR = "color"
        private const val ARG_ALPHA = "alpha"
        private const val ARG_PRESETS = "presets"
        private const val ARG_ALLOW_PRESETS = "allowPresets"
        private const val ARG_ALLOW_CUSTOM = "allowCustom"
        private const val ARG_DIALOG_TITLE = "dialogTitle"
        private const val ARG_SHOW_COLOR_SHADES = "showColorShades"
        private const val ARG_PRESETS_BUTTON_TEXT = "presetsButtonText"
        private const val ARG_CUSTOM_BUTTON_TEXT = "customButtonText"
        private const val ARG_SELECTED_BUTTON_TEXT = "selectedButtonText"

        fun newBuilder(): Builder {
            return Builder()
        }
    }
}