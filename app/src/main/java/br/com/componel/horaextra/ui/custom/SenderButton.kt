package br.com.componel.horaextra.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import br.com.componel.horaextra.R

class SenderButton(context: Context, attrs: AttributeSet) : Button(context, attrs) {
    companion object {
        private val STATE_SENDING = intArrayOf(R.attr.state_sending)
    }

    init {
        getStyleAttr(context, attrs).apply {
            try {
                textSending = getString(R.styleable.sender_textSending)!!
            } finally {
                recycle()
            }
        }
    }

    private fun getStyleAttr(context: Context, attrs: AttributeSet): TypedArray {
        return context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.sender,
            0, 0
        )
    }

    private lateinit var originalText: CharSequence
    lateinit var textSending: CharSequence
    var isSending = false
        set(value) {
            text = if (value) textSending else originalText
            isEnabled = !value
            field = value
        }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val estate = super.onCreateDrawableState(extraSpace + 1)
        if (isSending) {
            View.mergeDrawableStates(estate, STATE_SENDING)
        }
        return estate
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        originalText = text
    }
}