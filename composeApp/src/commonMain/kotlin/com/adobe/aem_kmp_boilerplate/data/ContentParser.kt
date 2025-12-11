package com.adobe.aem_kmp_boilerplate.data

import com.adobe.aem_kmp_boilerplate.network.edsJson
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Utility for parsing nested JSON content in EDS responses.
 */
object ContentParser {

    /**
     * Parse a JsonElement that could be either a string, array, or nested content
     */
    fun parseContentNodes(element: JsonElement?): List<ContentNode> {
        if (element == null) return emptyList()

        return when (element) {
            is JsonArray -> element.mapNotNull { parseNode(it) }
            is JsonObject -> listOfNotNull(parseNode(element))
            is JsonPrimitive -> {
                element.contentOrNull?.let {
                    listOf(ContentNode(type = "text", text = it))
                } ?: emptyList()
            }
        }
    }

    /**
     * Parse a single JsonElement into a ContentNode
     */
    private fun parseNode(element: JsonElement): ContentNode? {
        return when (element) {
            is JsonPrimitive -> {
                element.contentOrNull?.let {
                    ContentNode(type = "text", text = it)
                }
            }

            is JsonObject -> {
                try {
                    edsJson.decodeFromJsonElement(ContentNode.serializer(), element)
                } catch (e: Exception) {
                    null
                }
            }

            else -> null
        }
    }

    /**
     * Parse block content which is typically a 3D array:
     * [ rows [ columns [ content items ] ] ]
     */
    fun parseBlockContent(element: JsonElement?): List<BlockRow> {
        if (element == null) return emptyList()
        if (element !is JsonArray) return emptyList()

        return element.mapNotNull { rowElement ->
            if (rowElement is JsonArray) {
                val columns = rowElement.mapNotNull { colElement ->
                    if (colElement is JsonArray) {
                        BlockColumn(items = parseContentNodes(colElement))
                    } else {
                        null
                    }
                }
                BlockRow(columns = columns)
            } else {
                null
            }
        }
    }

    /**
     * Extract plain text from mixed content (handles nested strong, em, links, etc.)
     */
    fun extractPlainText(element: JsonElement?): String {
        if (element == null) return ""

        return when (element) {
            is JsonPrimitive -> element.contentOrNull ?: ""
            is JsonArray -> element.joinToString("") { extractPlainText(it) }
            is JsonObject -> {
                val text = element["text"]?.jsonPrimitive?.contentOrNull
                val content = element["content"]
                text ?: extractPlainText(content)
            }
        }
    }

    /**
     * Find first image in content
     */
    fun findFirstImage(nodes: List<ContentNode>): ContentNode? {
        return nodes.find { it.isImage }
            ?: nodes.flatMap { parseContentNodes(it.content) }.find { it.isImage }
    }

    /**
     * Find first link in content
     */
    fun findFirstLink(nodes: List<ContentNode>): ContentNode? {
        return nodes.find { it.isLink }
            ?: nodes.flatMap { parseContentNodes(it.content) }.find { it.isLink }
    }

    /**
     * Get heading text (handles both direct text and nested content)
     */
    fun getHeadingText(node: ContentNode): String? {
        return node.text ?: extractPlainText(node.content).takeIf { it.isNotBlank() }
    }
}

/**
 * Represents a row in block content
 */
data class BlockRow(
    val columns: List<BlockColumn> = emptyList()
) {
    val firstColumn: BlockColumn?
        get() = columns.firstOrNull()

    val secondColumn: BlockColumn?
        get() = columns.getOrNull(1)
}

/**
 * Represents a column in a block row
 */
data class BlockColumn(
    val items: List<ContentNode> = emptyList()
) {
    val firstImage: ContentNode?
        get() = ContentParser.findFirstImage(items)

    val firstLink: ContentNode?
        get() = ContentParser.findFirstLink(items)

    val textItems: List<ContentNode>
        get() = items.filter { it.isParagraph || it.isHeading || it.isStyledText }

    val plainText: String
        get() = items.joinToString("\n") {
            it.text ?: ContentParser.extractPlainText(it.content)
        }
}

