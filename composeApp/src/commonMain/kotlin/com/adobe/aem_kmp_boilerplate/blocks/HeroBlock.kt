package com.adobe.aem_kmp_boilerplate.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.adobe.aem_kmp_boilerplate.data.BlockRow
import com.adobe.aem_kmp_boilerplate.data.ContentParser
import com.adobe.aem_kmp_boilerplate.data.LocalEdsConfig
import com.adobe.aem_kmp_boilerplate.theme.Spacing

/**
 * Hero block variant based on EDS variations.
 */
enum class HeroVariant {
    Default,
    Small,
    Large,
    Centered
}

/**
 * Renders a hero block with image, title, subtitle, and optional CTA.
 */
@Composable
fun HeroBlock(
    blockName: String,
    rows: List<BlockRow>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val edsConfig = LocalEdsConfig.current
    val variant = determineVariant(blockName)
    val imageHeight = when (variant) {
        HeroVariant.Small -> 200.dp
        HeroVariant.Large -> 450.dp
        else -> 320.dp
    }

    // Extract content from first row
    val firstRow = rows.firstOrNull()
    val imageColumn = firstRow?.firstColumn
    val textColumn = firstRow?.secondColumn ?: firstRow?.firstColumn

    val imageUrl = edsConfig.resolveUrl(imageColumn?.firstImage?.src)
    val title = textColumn?.textItems?.firstOrNull()?.let {
        it.text ?: ContentParser.extractPlainText(it.content)
    }
    val subtitle = textColumn?.textItems?.drop(1)?.firstOrNull()?.let {
        it.text ?: ContentParser.extractPlainText(it.content)
    }
    val cta = textColumn?.firstLink

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) {
        // Background image
        imageUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }

        // Gradient overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 100f
                    )
                )
        )

        // Content overlay
        Column(
            modifier = Modifier
                .align(
                    when (variant) {
                        HeroVariant.Centered -> Alignment.Center
                        else -> Alignment.BottomStart
                    }
                )
                .padding(Spacing.lg),
            horizontalAlignment = when (variant) {
                HeroVariant.Centered -> Alignment.CenterHorizontally
                else -> Alignment.Start
            }
        ) {
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }

            subtitle?.let {
                Spacer(modifier = Modifier.height(Spacing.sm))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            cta?.let { link ->
                Spacer(modifier = Modifier.height(Spacing.md))
                Button(
                    onClick = { link.href?.let(onLinkClick) }
                ) {
                    Text(link.text ?: link.displayText ?: "Learn More")
                }
            }
        }
    }
}

private fun determineVariant(blockName: String): HeroVariant {
    return when {
        blockName.contains("small") -> HeroVariant.Small
        blockName.contains("large") -> HeroVariant.Large
        blockName.contains("centered") || blockName.contains("center") -> HeroVariant.Centered
        else -> HeroVariant.Default
    }
}
