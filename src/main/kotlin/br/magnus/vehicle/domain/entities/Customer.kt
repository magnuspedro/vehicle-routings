package br.magnus.vehicle.domain.entities

data class Customer(
    var id: Long,
    var location: Location,
    var demand: Long,
    val name: String? = null
)
