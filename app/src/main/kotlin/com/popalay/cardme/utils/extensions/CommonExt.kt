package com.popalay.cardme.utils.extensions

import android.content.Context
import android.net.Uri
import android.support.annotation.StringRes
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.popalay.cardme.R


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

fun Context.openLink(url: Uri) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(this, R.color.primary))
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, url)
}

fun Context.openLink(url: String) = openLink(Uri.parse(url))

fun Boolean.ifTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}

fun Boolean.ifFalse(block: () -> Unit) {
    if (!this) {
        block()
    }
}
