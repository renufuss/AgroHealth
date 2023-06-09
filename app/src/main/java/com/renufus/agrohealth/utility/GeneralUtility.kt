package com.renufus.agrohealth.utility

import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

class GeneralUtility {

    val delayLoading = 500L

    fun moveToAnotherActivity(context: Context, destinationActivity: Class<*>, flags: Int? = null) {
        val intent = Intent(context, destinationActivity)
        flags?.let { intent.flags = it }
        context.startActivity(intent)
    }

    fun setColoredText(textView: TextView, fullText: String, coloredTextList: List<Pair<String, Int>>) {
        val spannableStringBuilder = SpannableStringBuilder(fullText)
        for (coloredText in coloredTextList) {
            val startIndex = fullText.indexOf(coloredText.first)
            val endIndex = startIndex + coloredText.first.length
            spannableStringBuilder.setSpan(
                ForegroundColorSpan(coloredText.second),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
        textView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE)
    }

    fun setStatusBarColor(activity: AppCompatActivity, color: Int) {
        activity.window.statusBarColor = color
    }

    fun setButtonClickAnimation(button: View, animationResId: Int, onClickAction: () -> Unit) {
        button.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(button.context, animationResId)
            button.startAnimation(animation)
            onClickAction.invoke()
        }
    }

    fun validationEmail(email: String): Boolean {
        val pattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$",
        )
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun prettyCount(number: Int): String {
        if (number < 1000) {
            return number.toString()
        }

        val suffixes = listOf("", "k", "M", "B", "T")
        val magnitude = (Math.log10(number.toDouble()) / 3).toInt()
        val scaledNumber = number / Math.pow(10.0, (magnitude * 3).toDouble())
        val decimalPlaces = if (scaledNumber < 10) 1 else 0

        return String.format("%.${decimalPlaces}f%s", scaledNumber, suffixes[magnitude])
    }
}
