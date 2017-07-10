package com.popalay.cardme.injection

import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class SupportFragmentInjector(val androidInjector: AndroidInjector<Fragment>) : HasSupportFragmentInjector {
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = androidInjector
}
