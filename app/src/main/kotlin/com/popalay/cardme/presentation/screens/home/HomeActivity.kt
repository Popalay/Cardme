package com.popalay.cardme.presentation.screens.home

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.popalay.cardme.R
import com.popalay.cardme.business.ShortcutInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.databinding.ActivityHomeBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.presentation.screens.*
import com.popalay.cardme.presentation.screens.addcard.AddCardActivity
import com.popalay.cardme.presentation.screens.adddebt.AddDebtActivity
import com.popalay.cardme.presentation.screens.carddetails.CardDetailsActivity
import com.popalay.cardme.presentation.screens.cards.CardsFragment
import com.popalay.cardme.presentation.screens.debts.DebtsFragment
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsActivity
import com.popalay.cardme.presentation.screens.holders.HoldersFragment
import com.popalay.cardme.presentation.screens.settings.SettingsActivity
import com.popalay.cardme.presentation.screens.trash.TrashActivity
import com.popalay.cardme.utils.extensions.findFragmentByType
import com.popalay.cardme.utils.extensions.setSelectedItem
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.card.payment.CardIOActivity
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import shortbread.Shortcut
import javax.inject.Inject


class HomeActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var shortcutInteractor: ShortcutInteractor

    private lateinit var b: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var viewModelFacade: HomeViewModelFacade

    override var navigator = object : CustomNavigator(this, R.id.host) {

        override fun createFragment(screenKey: String, data: Any?) = when (screenKey) {
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

        @Suppress("UNCHECKED_CAST")
        override fun createActivityIntent(screenKey: String, data: Any?) = when (screenKey) {
            SCREEN_HOME -> HomeActivity.getIntent(activity)
            SCREEN_HOLDER_DETAILS -> HolderDetailsActivity.getIntent(activity, data as String)
            SCREEN_ADD_CARD -> AddCardActivity.getIntent(activity, data as Card)
            SCREEN_SCAN_CARD -> Intent(activity, CardIOActivity::class.java)
            SCREEN_SETTINGS -> SettingsActivity.getIntent(activity)
            SCREEN_ADD_DEBT -> AddDebtActivity.getIntent(activity)
            SCREEN_TRASH -> TrashActivity.getIntent(activity)
            SCREEN_CARD_DETAILS -> CardDetailsActivity.getIntent(activity, data as String)
            else -> null
        }

        override fun setupActivityTransactionAnimation(command: Command, activityIntent: Intent): Bundle? {
            if (command is Forward && command.screenKey == SCREEN_ADD_DEBT) {
                return findFragmentByType<DebtsFragment>()?.createAddDebtTransition(activityIntent)
            } else if (command is Forward && command.screenKey == SCREEN_CARD_DETAILS) {
                return findFragmentByType<CardsFragment>()?.createCardDetailsTransition(activityIntent)
            }
            return super.setupActivityTransactionAnimation(command, activityIntent)
        }
    }

    companion object {

        private val MENU_SETTINGS = Menu.FIRST

        fun getIntent(context: Context) = Intent(context, HomeActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        ViewModelProviders.of(this, factory).get(HomeViewModel::class.java).let {
            b.vm = it
            viewModelFacade = it
        }
        initUI()
    }

    public override fun onNewIntent(intent: Intent) {
        setIntent(intent)
    }

    public override fun onResume() {
        super.onResume()
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            processIntent(intent)
        }
    }

    fun processIntent(intent: Intent) {
        val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        val msg = rawMsgs [0] as NdefMessage
        val bytes = msg.records[0].payload
        viewModelFacade.onNfcMessageRead(bytes)
        getIntent().action = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CardsFragment.SCAN_REQUEST_CODE) {
            findFragmentByType<CardsFragment>()?.onActivityResult(requestCode, resultCode, data)
        }
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

    override fun onBackPressed() {
        if (b.drawerLayout.isDrawerOpen(Gravity.START)) {
            b.drawerLayout.closeDrawers()
            return
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        when (item.itemId) {
            MENU_SETTINGS -> b.vm?.settingClick?.accept(true)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun supportFragmentInjector() = androidInjector

    private fun initUI() {
        setSupportActionBar(b.toolbar)
        toggle = ActionBarDrawerToggle(this, b.drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled = true
        b.drawerLayout.addDrawerListener(toggle)
    }

    // Shortcuts
    @Shortcut(id = "SHORTCUT_ADD_CARD", icon = R.drawable.ic_shortcut_add_card, rank = 0, shortLabelRes = R.string.shortcut_add_card)
    fun addCardShortcut() {
        shortcutInteractor.applyShortcut(ShortcutInteractor.Shortcut.ADD_CARD)
    }

    @Shortcut(id = "SHORTCUT_ADD_DEBT", icon = R.drawable.ic_shortcut_debts, rank = 1, shortLabelRes = R.string.shortcut_add_debt)
    fun addDebtShortcut() {
        shortcutInteractor.applyShortcut(ShortcutInteractor.Shortcut.ADD_DEBT)
    }

    @Shortcut(id = "SHORTCUT_DEBTS", icon = R.drawable.ic_shortcut_debts, rank = 2, shortLabelRes = R.string.shortcut_debts)
    fun debtsShortcut() {
        shortcutInteractor.applyShortcut(ShortcutInteractor.Shortcut.DEBTS)
    }
}
