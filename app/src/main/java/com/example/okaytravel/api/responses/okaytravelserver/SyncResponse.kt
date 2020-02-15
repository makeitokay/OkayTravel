package com.example.okaytravel.api.responses.okaytravelserver

data class SyncResponse(
    val user: User?,
    val message: String?,
    val error: Boolean?
)

data class User(
    val user: UserInfo,
    val trips: List<Trip>
)

data class UserInfo(
    val username: String,
    val email: String,
    val passwordHash: String,
    val avatar: String,
    val accessToken: String,
    val commits: Int
)

data class Trip(
    val trip: TripInfo,
    val budget: List<BudgetElement>,
    val places: List<Place>
)

data class TripInfo(
    val remoteId: Long,
    val ownPlace: String,
    val startDate: String,
    val duration: Int
)

data class BudgetElement(
    val remoteId: Long,
    val amount: Int,
    val category: String
)

data class Place(
    val remoteId: Long,
    val name: String,
    val date: String
)