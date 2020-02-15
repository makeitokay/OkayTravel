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
    val remoteId: Long,
    val ownPlace: String?,
    val startDate: String?,
    val duration: Int?
)

data class BudgetElement(
    val remoteId: Long,
    val amount: Int?,
    val category: String?
)

data class Place(
    val remoteId: Long,
    val name: String?,
    val date: String?
)