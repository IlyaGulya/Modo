package com.github.terrakok.modo.androidApp.deeplink

import android.net.Uri
import com.github.terrakok.modo.*
import com.github.terrakok.modo.androidApp.Screens

data class Deeplink(
    val uri: Uri,
) : NavigationAction

class DeeplinkDirectStateModifyingReducer(
    private val origin: NavigationReducer,
) : NavigationReducer {
    override fun invoke(action: NavigationAction, state: NavigationState): NavigationState =
        when (action) {
            is Deeplink -> handleDeeplink(action, state)
            else -> origin.invoke(action, state)
        }

    private fun handleDeeplink(action: Deeplink, state: NavigationState): NavigationState {
        val uri = action.uri
        return when (uri.host) {
            "multi" -> handleMultiStackDeeplink(uri, state)
            else -> state
        }
    }

    private fun handleMultiStackDeeplink(
        uri: Uri,
        state: NavigationState,
    ): NavigationState {
        val path = uri.pathSegments.toMutableList()
        val hasPath = path.isNotEmpty()
        if (path.isEmpty()) return state

        val tabId = path.removeAt(0).toInt()
        val screensInsideTab = path.map { Screens.Tab(tabId, it.toInt()) }

        return NavigationState(chain = sequence {
            yield(Screens.Start())

            yield(
                Screens.MultiStack(
                    Screens.TabState(
                        tabId,
                        NavigationState(screensInsideTab)
                    )
                )
            )
        }.toList()
        )
    }

}

class DeeplinkActionEmittingReducer(
    private val origin: NavigationReducer,
) : NavigationReducer {
    override fun invoke(action: NavigationAction, state: NavigationState): NavigationState =
        when (action) {
            is Deeplink -> handleDeeplink(action, state)
            else -> origin.invoke(action, state)
        }

    private fun handleDeeplink(action: Deeplink, state: NavigationState): NavigationState {
        val uri = action.uri
        return when (uri.host) {
            "multi" -> handleMultiStackDeeplink(uri, state)
            else -> state
        }
    }

    private fun handleMultiStackDeeplink(
        uri: Uri,
        state: NavigationState,
    ): NavigationState {
        val path = uri.pathSegments
        if (path.isEmpty()) return state

        val tabId = path.first().toInt()
        val selectedTabState = origin.invoke(SelectStack(tabId), state)
        val screensInsideTab = path.drop(1).map { Screens.Tab(tabId, it.toInt()) }.toMutableList()

        if (screensInsideTab.isEmpty()) return selectedTabState

        val tabRootState = origin.invoke(BackToTabRoot, selectedTabState)
        val firstScreen = screensInsideTab.first()
        val newStack = NewStack(firstScreen, *screensInsideTab.drop(1).toTypedArray())
        return origin.invoke(newStack, tabRootState)
    }

}