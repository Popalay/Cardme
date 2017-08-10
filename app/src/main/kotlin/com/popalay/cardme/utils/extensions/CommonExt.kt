package com.popalay.cardme.utils.extensions

import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity


fun FragmentActivity.currentFragment() = supportFragmentManager.fragments?.filter { it.isVisible }?.firstOrNull()

inline fun <reified T : Fragment> AppCompatActivity.findFragmentByType() = supportFragmentManager.fragments
        ?.filter { it is T }
        ?.map { it as T }
        ?.firstOrNull()

fun FragmentActivity.shareText(@StringRes title: Int, text: String) {
    val intent = ShareCompat.IntentBuilder.from(this)
            .setChooserTitle(title)
            .setType("text/plain")
            .setText(text)
            .createChooserIntent()
    if (intent.resolveActivity(this.packageManager) != null) {
        this.startActivity(intent)
    }
}

fun Fragment.shareText(@StringRes title: Int, text: String) = activity.shareText(title, text)
