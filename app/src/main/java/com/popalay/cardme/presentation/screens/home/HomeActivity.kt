package com.popalay.cardme.presentation.screens.home

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.popalay.cardme.PRIVACY_POLICY_LINK
import com.popalay.cardme.R
import com.popalay.cardme.business.ShortcutInteractor
import com.popalay.cardme.databinding.ActivityHomeBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.presentation.screens.*
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity
import com.popalay.cardme.presentation.screens.cards.CardsFragment
import com.popalay.cardme.presentation.screens.debts.DebtsFragment
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsActivity
import com.popalay.cardme.presentation.screens.holders.HoldersFragment
import com.popalay.cardme.presentation.screens.settings.SettingsActivity
import com.popalay.cardme.utils.BrowserUtils
import com.popalay.cardme.utils.extensions.setSelectedItem
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import ru.terrakok.cicerone.NavigatorHolder
import shortbread.Shortcut
import javax.inject.Inject

class HomeActivity : BaseActivity(), HomeView, HasSupportFragmentInjector {

    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var navigationHolder: NavigatorHolder
    @Inject lateinit var shortcutInteractor: ShortcutInteractor

    @InjectPresenter lateinit var presenter: HomePresenter

    private lateinit var b: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private var startPage = R.id.cards

    companion object {

        private val MENU_SETTINGS = Menu.FIRST

        fun getIntent(context: Context) = Intent(context, HomeActivity::class.java)

    }

    @ProvidePresenter fun providePresenter() = HomePresenter(startPage)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        initUI()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, R.string.settings)
                .setIcon(R.drawable.ic_settings)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        when (item.itemId) {
            MENU_SETTINGS -> presenter.onSettingsClick()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun openPolicy() {
        BrowserUtils.openLink(this, PRIVACY_POLICY_LINK)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return androidInjector
    }

    private val navigator = object : CustomNavigator(this, R.id.host) {

        override fun createFragment(screenKey: String, data: Any?): Fragment? {
            return when (screenKey) {
                SCREEN_CARDS -> {
                    b.bottomBar.setSelectedItem(R.id.cards, false)
                    CardsFragment.newInstance()
                }
                SCREEN_HOLDERS -> {
                    b.bottomBar.setSelectedItem(R.id.holders, false)
                    HoldersFragment.newInstance()
                }
                SCREEN_DEBTS -> {
                    b.bottomBar.setSelectedItem(R.id.debts, false)
                    DebtsFragment.newInstance()
                }
                else -> null
            }
        }

        override fun createActivityIntent(screenKey: String, data: Any?): Intent? {
            return when (screenKey) {
                SCREEN_HOLDER_DETAILS -> HolderDetailsActivity.getIntent(this@HomeActivity, data as String)
                SCREEN_ADD_CARD -> AddCardActivity.getIntent(this@HomeActivity, data as CreditCard)
                SCREEN_SCAN_CARD -> Intent(this@HomeActivity, CardIOActivity::class.java)
                SCREEN_SETTINGS -> SettingsActivity.getIntent(this@HomeActivity)
                else -> null
            }
        }
    }

    private fun onNavigationClick(item: MenuItem): Boolean {
        presenter.onBottomNavigationItemClick(item.itemId)
        return true
    }

    private fun onDrawerItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_privacy_policy -> presenter.onPolicyClick()
        }
        b.drawerLayout.closeDrawers()
        return true
    }

    private fun initUI() {
        setSupportActionBar(b.toolbar)
        toggle = ActionBarDrawerToggle(this, b.drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled = true
        b.drawerLayout.addDrawerListener(toggle)
        b.navigationView.setNavigationItemSelectedListener(this::onDrawerItemClick)
        b.bottomBar.setOnNavigationItemSelectedListener(this::onNavigationClick)
    }

    // Shortcuts
    @Shortcut(id = "SHORTCUT_ADD_CARD", icon = R.drawable.ic_shortcut_add_card, shortLabelRes = R.string.shortcut_add_card)
    fun addCardShortcut() {
        shortcutInteractor.applyShortcut(ShortcutInteractor.Shortcut.ADD_CARD)
    }

    @Shortcut(id = "SHORTCUT_FAVORITE_HOLDER", icon = R.drawable.ic_shortcut_favorite_holder, rank = 1, shortLabelRes = R.string.shortcut_favorite_holder)
    fun favoriteHolderShortcut() {
        shortcutInteractor.applyShortcut(ShortcutInteractor.Shortcut.FAVORITE_HOLDER)
    }

    @Shortcut(id = "SHORTCUT_DEBTS", icon = R.drawable.ic_shortcut_debts, rank = 2, shortLabelRes = R.string.shortcut_debts)
    fun debtsShortcut() {
        shortcutInteractor.applyShortcut(ShortcutInteractor.Shortcut.DEBTS)
    }
}
