package com.popalay.cardme.presentation.screens.home

import android.app.PendingIntent
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.popalay.cardme.R
import com.popalay.cardme.domain.interactor.ShortcutInteractor
import com.popalay.cardme.databinding.ActivityHomeBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.presentation.screens.cards.CardsFragment
import com.popalay.cardme.utils.extensions.findFragmentByType
import com.popalay.cardme.utils.extensions.getDataBinding
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.setSelectedItem
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import shortbread.Shortcut
import javax.inject.Inject


class HomeActivity : BaseActivity(), HasSupportFragmentInjector {

    companion object {
        private val MENU_SETTINGS = Menu.FIRST
        fun getIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject override lateinit var navigator: CustomNavigator
    @Inject lateinit var shortcutInteractor: ShortcutInteractor
    @Inject lateinit var viewModelFacade: HomeViewModelFacade

    private lateinit var b: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private var adapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var filters: Array<IntentFilter>? = null
    private var techLists: Array<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = getDataBinding(R.layout.activity_home)
        b.vm = getViewModel<HomeViewModel>(factory)
        initNfcListening()
        initUI()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onNewIntent(intent: Intent) = processIntent(intent)

    public override fun onResume() {
        super.onResume()
        adapter?.enableForegroundDispatch(this, pendingIntent, filters, techLists)
    }

    override fun onPause() {
        super.onPause()
        adapter?.disableForegroundDispatch(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CardsFragment.SCAN_REQUEST_CODE) {
            findFragmentByType<CardsFragment>()?.onActivityResult(requestCode, resultCode, data)
        }
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
            MENU_SETTINGS -> viewModelFacade.onSettingClick()
        }
        return super.onOptionsItemSelected(item)
    }

    fun selectTab(itemId: Int) {
        b.bottomBar.setSelectedItem(itemId, false)
    }

    override fun supportFragmentInjector() = androidInjector

    private fun initUI() {
        setSupportActionBar(b.toolbar)
        toggle = ActionBarDrawerToggle(this, b.drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled = true
        b.drawerLayout.addDrawerListener(toggle)
    }

    //TODO create specific class
    private fun initNfcListening() {
        adapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP), 0)
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            ndef.addDataType("application/" + packageName)
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("fail", e)
        }
        filters = arrayOf(ndef)
        techLists = arrayOf((arrayOf<String>(NfcF::class.java.name)))
    }

    private fun processIntent(intent: Intent) {
        val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        val msg = rawMsgs [0] as NdefMessage
        val bytes = msg.records[0].payload
        viewModelFacade.onNfcMessageRead(bytes)
        getIntent().action = null
    }

    // Shortcuts
    @Shortcut(id = "SHORTCUT_ADD_CARD", icon = R.drawable.ic_shortcut_add_card, rank = 0, shortLabelRes = R.string.shortcut_add_card)
    fun addCardShortcut() {
        shortcutInteractor.applyShortcut(ShortcutInteractor.Shortcut.ADD_CARD)
    }

    @Shortcut(id = "SHORTCUT_DEBTS", icon = R.drawable.ic_shortcut_debts, rank = 2, shortLabelRes = R.string.shortcut_debts)
    fun debtsShortcut() {
        shortcutInteractor.applyShortcut(ShortcutInteractor.Shortcut.DEBTS)
    }
}
