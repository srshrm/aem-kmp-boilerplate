package com.adobe.aem_kmp_boilerplate.blocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adobe.aem_kmp_boilerplate.data.BlockRow
import com.adobe.aem_kmp_boilerplate.theme.Spacing

/**
 * Generic block renderer for unrecognized block types.
 * Renders content in a simple vertical layout.
 */
@Composable
fun GenericBlock(
    blockName: String,
    rows: List<BlockRow>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.md),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md)
        ) {
            // Block name as header (for debugging/development)
            if (blockName.isNotBlank()) {
                Text(
                    text = blockName.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = Spacing.sm)
                )
            }

            // Render all content rows
            rows.forEach { row ->
                row.columns.forEach { column ->
                    column.items.forEach { item ->
                        ContentNodeRenderer(
                            node = item,
                            onLinkClick = onLinkClick
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Spacing.sm))
            }
        }
    }
}
