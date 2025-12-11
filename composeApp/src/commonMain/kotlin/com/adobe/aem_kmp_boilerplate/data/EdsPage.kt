package com.adobe.aem_kmp_boilerplate.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Represents an EDS page with its content structure.
 * Based on actual JSON from: https://mhast-html-to-json.adobeaem.workers.dev/
 */
@Serializable
data class EdsPage(
    val metadata: PageMetadata = PageMetadata(),
    val content: List<SectionContainer> = emptyList()
)

/**
 * Container for a section with optional section-level metadata.
 */
@Serializable
data class SectionContainer(
    val metadata: SectionMetadata? = null,
    val section: List<List<ContentNode>> = emptyList()
)

/**
 * Section-level metadata (e.g., style: "highlight")
 */
@Serializable
data class SectionMetadata(
    val style: String? = null
)

/**
 * Page metadata extracted from the EDS page.
 */
@Serializable
data class PageMetadata(
    val title: MetadataText? = null,
    val canonical: MetadataHref? = null,
    val meta: List<MetaTag>? = null,
    val script: JsonElement? = null,
    val stylesheet: MetadataHref? = null
)

@Serializable
data class MetadataText(
    val text: String? = null
)

@Serializable
data class MetadataHref(
    val href: String? = null
)

@Serializable
data class MetaTag(
    val tag: String? = null,
    val text: String? = null
)

/**
 * Extension to get page title as string
 */
val PageMetadata.titleText: String?
    get() = title?.text

/**
 * Extension to get description from meta tags
 */
val PageMetadata.description: String?
    get() = meta?.find { it.tag == "description" }?.text

/**
 * Extension to get OG image from meta tags
 */
val PageMetadata.ogImage: String?
    get() = meta?.find { it.tag == "og:image" }?.text
