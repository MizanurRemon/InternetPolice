package com.internetpolice.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NewsDto(
    val date: String,
    val date_gmt: String,
    val guid: Guid? = null,
    val id: Int,
    val link: String,
    val modified: String,
    val modified_gmt: String,
    val slug: String,
    val status: String,
    val title: Title? = null,
    val type: String,
    val yoast_head_json: YoastHeadJSON? = null,
    val content: Content? = null
)


@Serializable
data class Content(var rendered: String)

@Serializable
data class Title(
    val rendered: String
)

@Serializable
data class Guid(
    val rendered: String
)

@Serializable
data class YoastHeadJSON(
    val title: String,
    val canonical: String,
    val og_locale: String,
    val og_type: String,
    val og_title: String,
    val og_description: String,
    val og_url: String,
    val og_site_name: String,
    val article_published_time: String,
    val article_modified_time: String? = "",
    val og_image: List<OgImage>? = null,
    val author: String,
    val twitter_card: String,
    val schema: Schema
)

@Serializable
data class OgImage(
    val width: Long? = null,
    val height: Long? = null,
    val url: String? = null,
    val type: String? = null
)

@Serializable
data class Schema(
    @SerialName("@context")
    val context: String,
    @SerialName("@graph")
    val graph: List<Graph>
)


@Serializable
data class Graph(
    @SerialName("@type")
    val type: String,
    @SerialName("@id")
    val id: String,
    val author: Author? = null,
    val headline: String? = null,
    val datePublished: String? = null,
    val dateModified: String? = null,
    val wordCount: Long? = null,
    val articleSection: List<String>? = null,
    val inLanguage: String? = null,
    val url: String? = null,
    val name: String? = null,
    val description: String? = null,
    val sameAs: List<String>? = null
)

@Serializable
data class Author(
    val name: String,
    @SerialName("@id")
    val id: String
) 
