package br.magnus.vehicle.domain.geo

import br.magnus.vehicle.domain.entities.Location
import kotlin.math.ceil
import kotlin.math.sqrt

class EuclideanDistanceCalculator : DistanceCalculator {
    companion object {
        private const val METERS_PER_DEGREE: Long = 111_000
    }

    override fun calculateDistance(from: Location, to: Location): Long {
        if (from == to) {
            return 0L
        }
        val latitudeDiff: Double = to.latitude - from.latitude
        val longitudeDiff: Double = to.longitude - from.longitude
        return ceil(sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff) * METERS_PER_DEGREE).toLong()
    }


}