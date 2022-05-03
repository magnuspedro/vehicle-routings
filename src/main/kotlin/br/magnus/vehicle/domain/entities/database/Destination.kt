package br.magnus.vehicle.domain.entities.database

data class Destination(
    val destination: String? = null,
    val destinationState: String? = null,
    var destinationLatitude: Double? = null,
    var destinationLongitude: Double? = null,
    val loadType: String? = null,
    val weight: Long? = null
)