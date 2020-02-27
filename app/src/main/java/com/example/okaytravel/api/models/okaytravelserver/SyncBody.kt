package com.example.okaytravel.api.models.okaytravelserver

import java.util.*

data class SyncBody(
    val user: User,
    val accessToken: String
)

data class User(
    val user: UserInfo?,
    val trips: ArrayList<Trip>?
)

data class UserInfo(
    val username: String?,
    val email: String?,
    val passwordHash: String?,
    val avatar: String?,
    val commits: Int?
)

data class Trip(
    val trip: TripInfo?,
    val budget: ArrayList<BudgetElement>?,
    val places: ArrayList<Place>?
)

data class TripInfo(
    val uuid: String?,
    val ownPlace: String?,
    val fullAddress: String?,
    val startDate: String?,
    val duration: Int?
)

data class BudgetElement(
    val uuid: String?,
    val amount: Int?,
    val category: String?
)

data class Place(
    val uuid: String?,
    val name: String?,
    val fullAddress: String?,
    val latitude: String?,
    val longitude: String?,
    val date: String?
)