package com.adobe.aem_kmp_boilerplate.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Represents a content node in the EDS JSON structure.
 * Content nodes can be paragraphs, headings, images, links, blocks, lists, etc.
 *
 * Based on actual JSON from: https://mhast-html-to-json.adobeaem.workers.dev/adobe/aem-boilerplate
 */
@Serializable
data class ContentNode(
    val type: String? = null,

    // For text content
    val text: String? = null,

    // For headings
    val level: Int? = null,

    // For images
    val src: String? = null,
    val alt: String? = null,

    // For links
    val href: String? = null,

    // For blocks (columns, cards, etc.)
    val name: String? = null,

    // For nested content (paragraphs with mixed content, blocks, etc.)
    val content: JsonElement? = null,

    // For lists
    val ordered: Boolean? = null,
    val items: List<ListItem>? = null,

    // For HTML tags like <br>, <code>
    val tag: String? = null
) {
    /**
     * Check if this is a block element
     */
    val isBlock: Boolean
        get() = type == "block"

    /**
     * Check if this is a heading
     */
    val isHeading: Boolean
        get() = type == "heading"

    /**
     * Check if this is an image
     */
    val isImage: Boolean
        get() = type == "image"

    /**
     * Check if this is a link
     */
    val isLink: Boolean
        get() = type == "link"

    /**
     * Check if this is a paragraph
     */
    val isParagraph: Boolean
        get() = type == "paragraph"

    /**
     * Check if this is a list
     */
    val isList: Boolean
        get() = type == "list"

    /**
     * Check if this is styled text (strong, em, etc.)
     */
    val isStyledText: Boolean
        get() = type == "strong" || type == "em"

    /**
     * Get display text from various sources
     */
    val displayText: String?
        get() = text ?: alt
}

/**
 * List item in a list content node
 */
@Serializable
data class ListItem(
    val type: String? = null,
    val text: String? = null
)

