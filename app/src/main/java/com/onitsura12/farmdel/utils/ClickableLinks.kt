package com.onitsura12.farmdel.utils

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.View

class ClickableLinks {

    object MakeLinksClickable {
        private val LOG = MakeLinksClickable::class.java.simpleName
        fun reformatText(text: CharSequence): SpannableStringBuilder {
            val end = text.length
            val sp: Spannable = text as Spannable
            val urls: Array<URLSpan> = sp.getSpans(0, end, URLSpan::class.java)
            val style = SpannableStringBuilder(text)
            for (url in urls) {
                style.removeSpan(url)
                val click = CustomerTextClick(url.getURL())
                style.setSpan(
                    click, sp.getSpanStart(url), sp.getSpanEnd(url),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return style
        }

        class CustomerTextClick(var mUrl: String) : ClickableSpan() {
            override fun onClick(widget: View) {
                //Тут можно как-то обработать нажатие на ссылку
                //Сейчас же мы просто открываем браузер с ней
                Log.i(LOG, "url clicked: " + mUrl)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(mUrl)
                widget.context.startActivity(i)
            }
        }
    }
}