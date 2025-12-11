package com.adobe.aem_kmp_boilerplate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.adobe.aem_kmp_boilerplate.data.DefaultEdsConfig
import com.adobe.aem_kmp_boilerplate.data.EdsConfig
import com.adobe.aem_kmp_boilerplate.screens.HomeScreen
import com.adobe.aem_kmp_boilerplate.screens.PageDetailScreen

/**
 * Main navigation host using Navigation 3 with NavDisplay.
 *
 * @param edsConfig The EDS site configuration
 * @param onExternalLink Callback for handling external links (opens browser)
 */
@Composable
fun AppNavigation(
    edsConfig: EdsConfig = DefaultEdsConfig,
    onExternalLink: (String) -> Unit = {}
) {
    val backStack = remember { mutableStateListOf<Any>(Home) }

    AppNavigationContent(
        backStack = backStack,
        edsConfig = edsConfig,
        onExternalLink = onExternalLink
    )
}

@Composable
private fun AppNavigationContent(
    backStack: SnapshotStateList<Any>,
    edsConfig: EdsConfig,
    onExternalLink: (String) -> Unit
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { route ->
            when (route) {
                is Home -> NavEntry(route) {
                    HomeScreen(
                        edsConfig = edsConfig,
                        onNavigate = { url ->
                            handleLinkClick(
                                url = url,
                                edsConfig = edsConfig,
                                backStack = backStack,
                                onExternalLink = onExternalLink
                            )
                        }
                    )
                }

                is PageDetail -> NavEntry(route) {
                    PageDetailScreen(
                        path = route.path,
                        edsConfig = edsConfig,
                        onNavigate = { url ->
                            handleLinkClick(
                                url = url,
                                edsConfig = edsConfig,
                                backStack = backStack,
                                onExternalLink = onExternalLink
                            )
                        },
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                else -> NavEntry(Unit) {
                    // Fallback - navigate to home
                    HomeScreen(
                        edsConfig = edsConfig,
                        onNavigate = { url ->
                            handleLinkClick(
                                url = url,
                                edsConfig = edsConfig,
                                backStack = backStack,
                                onExternalLink = onExternalLink
                            )
                        }
                    )
                }
            }
        }
    )
}

/**
 * Handle a link click, determining whether to navigate internally or externally.
 */
private fun handleLinkClick(
    url: String,
    edsConfig: EdsConfig,
    backStack: SnapshotStateList<Any>,
    onExternalLink: (String) -> Unit
) {
    when {
        // Skip anchor links for now
        LinkHandler.isAnchorLink(url) -> {
            // TODO: Implement anchor scrolling
        }

        // Handle special protocols (mailto, tel) externally
        LinkHandler.isSpecialProtocol(url) -> {
            onExternalLink(url)
        }

        // Internal navigation
        LinkHandler.shouldNavigateInternally(url, edsConfig) -> {
            val path = LinkHandler.extractPath(url, edsConfig)
            if (path.isEmpty() || path == "index") {
                // Navigate to home if not already there
                if (backStack.lastOrNull() !is Home) {
                    backStack.add(Home)
                }
            } else {
                backStack.add(PageDetail(path))
            }
        }

        // External link - open in browser
        else -> {
            onExternalLink(url)
        }
    }
}
