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
    val commits: Int,
    val premium: Boolean
)

data class Trip(
    val trip: TripInfo,
    val budget: List<BudgetElement>,
    val places: List<Place>,
    val things: List<Thing>
)

data class TripInfo(
    val uuid: String,
    val ownPlace: String,
    val fullAddress: String,
    val startDate: String,
    val duration: Int
)

data class Thing(
    val uuid: String,
    val name: String,
    val taken: Boolean
)

data class BudgetElement(
    val uuid: String,
    val amount: Int,
    val category: String
)

data class Place(
    val uuid: String,
    val name: String,
    val fullAddress: String,
    val latitude: String,
    val longitude: String,
    val date: String
)