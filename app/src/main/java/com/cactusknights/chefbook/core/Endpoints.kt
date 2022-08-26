package com.cactusknights.chefbook.core

object Endpoints {
    private const val HTTP = "http://"
    private const val HTTPS = "https://"
    private const val DOMAIN = "chefbook.space"
    private const val WWW_DOMAIN = "www.chefbook.space"

    private const val BASE_URL = "${HTTPS}${DOMAIN}"
    const val RECIPES_ENDPOINT = "$BASE_URL/recipes"

    private val baseUrlVariants = listOf(
        BASE_URL,
        "${HTTPS}${WWW_DOMAIN}",
        "${HTTP}${DOMAIN}",
        "${HTTP}${WWW_DOMAIN}",
    )

    val recipesEndpointVariants = baseUrlVariants.map { baseUrl -> "${baseUrl}/recipes" }
}
