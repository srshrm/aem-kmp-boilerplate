package com.adobe.aem_kmp_boilerplate.blocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adobe.aem_kmp_boilerplate.data.ContentNode
import com.adobe.aem_kmp_boilerplate.theme.Spacing

/**
 * Renders default content items (content outside of blocks).
 * These are typically headings, paragraphs, images, etc. directly in a section.
 */
@Composable
fun DefaultContentRenderer(
    items: List<ContentNode>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.md)
    ) {
        items.forEach { item ->
            ContentNodeRenderer(
                node = item,
                onLinkClick = onLinkClick
            )
            Spacer(modifier = Modifier.height(Spacing.xs))
        }
    }
}

/**
 * Renders a section which contains multiple content item groups.
 * Each group is a list of content nodes.
 */
@Composable
fun SectionRenderer(
    contentGroups: List<List<ContentNode>>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.sm)
    ) {
        contentGroups.forEach { group ->
            group.forEach { node ->
                if (node.isBlock) {
                    // Render as a block
                    BlockRenderer(
                        block = node,
                        onLinkClick = onLinkClick
                    )
                } else {
                    // Render as default content
                    ContentNodeRenderer(
                        node = node,
                        onLinkClick = onLinkClick,
                        modifier = Modifier.padding(horizontal = Spacing.md)
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing.sm))
        }
    }
}
