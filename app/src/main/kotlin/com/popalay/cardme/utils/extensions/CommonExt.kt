package com.popalay.cardme.utils.extensions

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
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

fun FragmentActivity.openShareChooser(@StringRes title: Int, text: String) {
    val intent = ShareCompat.IntentBuilder.from(this)
            .setChooserTitle(title)
            .setType("text/plain")
            .setText(text)
            .createChooserIntent()
    if (intent.resolveActivity(this.packageManager) != null) {
        this.startActivity(intent)
    }
}

fun FragmentActivity.shareUsingNfc(@StringRes title: Int, text: String) {
    val targetShareIntents = mutableListOf<Intent>()
    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.type = "text/plain"
    val resInfos = packageManager.queryIntentActivities(shareIntent, 0)
    if (!resInfos.isEmpty()) {
        for (resInfo in resInfos) {
            val packageName = resInfo.activityInfo.packageName
            if (packageName.contains("nfc")) {
                val intent = Intent()
                intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, text)
                intent.`package` = packageName
                targetShareIntents.add(intent)
            }
        }
        if (!targetShareIntents.isEmpty()) {
            val chooserIntent = Intent.createChooser(targetShareIntents.removeAt(0), getString(title))
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toTypedArray())
            startActivity(chooserIntent)
        }
    }
}

fun Context.createNdefMessage(byteArray: ByteArray): NdefMessage {
    return NdefMessage(arrayOf(NdefRecord.createMime("application/" + packageName, byteArray),
            NdefRecord.createApplicationRecord(packageName)))
}

fun Fragment.openShareChooser(@StringRes title: Int, text: String) = activity.openShareChooser(title, text)

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