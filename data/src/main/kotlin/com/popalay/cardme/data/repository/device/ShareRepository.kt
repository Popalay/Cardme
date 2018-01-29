package com.popalay.cardme.data.repository.device

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.popalay.cardme.domain.repository.ShareRepository
import dagger.Reusable
import io.reactivex.Completable
import javax.inject.Inject

@Reusable
class ShareRepository @Inject constructor(
	private val context: Context
) : ShareRepository {

	override fun shareByNfc(content: String): Completable = Completable.fromAction {
		val targetShareIntents = mutableListOf<Intent>()
		val shareIntent = Intent()
		shareIntent.action = Intent.ACTION_SEND
		shareIntent.type = "text/plain"
		val resInfos = context.packageManager.queryIntentActivities(shareIntent, 0)
		if (!resInfos.isEmpty()) {
			for (resInfo in resInfos) {
				val packageName = resInfo.activityInfo.packageName
				if (packageName.contains("nfc")) {
					val intent = Intent()
					intent.component = ComponentName(packageName, resInfo.activityInfo.name)
					intent.action = Intent.ACTION_SEND
					intent.type = "text/plain"
					intent.putExtra(Intent.EXTRA_TEXT, content)
					intent.`package` = packageName
					targetShareIntents.add(intent)
				}
			}
			if (!targetShareIntents.isEmpty()) {
				val chooserIntent = Intent.createChooser(targetShareIntents.removeAt(0), null)
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toTypedArray())
				context.startActivity(chooserIntent)
			}
		}
	}
}
