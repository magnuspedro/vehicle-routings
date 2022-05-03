package br.magnus.vehicle.domain.entities.database

data class Origin(
    val origin: String? = null,
    val originState: String? = null,
    var originLatitude: Double? = null,
    var originLongitude: Double? = null,
)
