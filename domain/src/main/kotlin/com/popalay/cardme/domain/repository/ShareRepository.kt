package com.popalay.cardme.domain.repository

import io.reactivex.Completable

interface ShareRepository {

	fun shareByNfc(content: String): Completable
}
