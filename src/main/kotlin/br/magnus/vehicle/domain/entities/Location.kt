package br.magnus.vehicle.domain.entities

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import kotlin.math.atan2

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonIgnoreProperties("id")
class Location(
    var id: Long,
    var latitude: Double,
    var longitude: Double,
    @JsonDeserialize(keyUsing = LocationDescerializer::class)
    var distanceMap: Map<Location, Long>? = emptyMap()
) {

    fun getDistanceTo(location: Location): Long? {
        return distanceMap?.get(location)
    }

    fun getAngle(location: Location): Double {
        val latitudeDifference = location.latitude - latitude
        val longitudeDifference = location.longitude - longitude
        return atan2(latitudeDifference, longitudeDifference)
    }

}