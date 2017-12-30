/*
 * Created by popalay on 30.12.17 22:12
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 22:12
 */

package com.popalay.cardme.domain.usecase

import io.reactivex.ObservableTransformer

/**
 * Created by Denys Nykyforov on 30.12.2017
 * Copyright (c) 2017. All right reserved
 */
interface Action

interface Result

internal typealias UseCase<A> = ObservableTransformer<A, Result>

typealias ActionTransformer = ObservableTransformer<Action, Result>