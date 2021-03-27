package com.github.terrakok.modo.androidApp

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.github.terrakok.modo.MultiScreen
import com.github.terrakok.modo.NavigationState
import com.github.terrakok.modo.android.AppScreen
import com.github.terrakok.modo.android.ExternalScreen
import com.github.terrakok.modo.android.FlowAppScreen
import com.github.terrakok.modo.android.MultiAppScreen
import com.github.terrakok.modo.androidApp.fragment.CommandsFragment
import com.github.terrakok.modo.androidApp.fragment.StartFragment
import com.github.terrakok.modo.androidApp.fragment.TabFragment
import kotlinx.parcelize.Parcelize

object Screens {
    @Parcelize
    class Start : AppScreen("Start") {
        override fun create() = StartFragment()
    }

    @Parcelize
    class Commands(private val i: Int) : AppScreen("c_$i") {
        override fun create() = CommandsFragment.create(i)
    }

    @Parcelize
    class Deeplinks(private val i: Int): AppScreen("Deeplinks$i") {
        override fun create(): Fragment {
            TODO("Not yet implemented")
        }
    }

    @Parcelize
    class Tab(val tabId: Int, val i: Int) : AppScreen("t_$i") {
        override fun create() = TabFragment.create(tabId, i)
    }

    fun MultiStack(initialTabState: TabState? = null): MultiScreen {
        val states =
            listOf(Tab(0, 1), Tab(1, 1), Tab(2, 1))
                .associateBy { it.tabId }
                .mapValues { NavigationState(listOf(it.value)) }
        val stacks = if (initialTabState != null) {
            val (tabId, state) = initialTabState
            val newMap = states + (tabId to state)
            newMap.values.toList()
        } else {
            states.values.toList()
        }
        return MultiAppScreen(
            id = "MultiStack",
            stacks = stacks,
            selected = initialTabState?.tabId ?: 1
        )
    }

    fun FlowScreen() = FlowAppScreen(
        "Flow",
        Start()
    )

    fun Browser(url: String) = ExternalScreen {
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }

    data class TabState(
        val tabId: Int,
        val state: NavigationState
    )
}