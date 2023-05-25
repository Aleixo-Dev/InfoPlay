package com.nicolas.freegames.models.remote


import com.google.gson.annotations.SerializedName

data class GameDetailResponse(
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("developer")
    val developer: String? = null,
    @SerializedName("freetogame_profile_url")
    val freetogameProfileUrl: String? = null,
    @SerializedName("game_url")
    val gameUrl: String? = null,
    @SerializedName("genre")
    val genre: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("minimum_system_requirements")
    val minimumSystemRequirements: MinimumSystemRequirements? = null,
    @SerializedName("platform")
    val platform: String? = null,
    @SerializedName("publisher")
    val publisher: String? = null,
    @SerializedName("release_date")
    val releaseDate: String? = null,
    @SerializedName("screenshots")
    val screenshots: List<Screenshot>? = null,
    @SerializedName("short_description")
    val shortDescription: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("title")
    val title: String? = null
)