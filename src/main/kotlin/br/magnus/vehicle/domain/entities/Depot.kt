package br.magnus.vehicle.domain.entities

data class Depot(
    val id: Long,
    var location: Location,
    val name: String? = null
)
