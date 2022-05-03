package br.magnus.vehicle.domain.geo

import br.magnus.vehicle.domain.entities.Location
import java.util.function.Function
import java.util.stream.Collectors

interface DistanceCalculator {

    fun calculateDistance(from: Location, to: Location): Long

    fun calculateBulkDistance(
        fromLocations: Collection<Location?>,
        toLocations: Collection<Location?>
    ): MutableMap<Location, MutableMap<Location, Long>> {
        return fromLocations.stream().collect(Collectors.toMap(
            Function.identity()
        ) { from ->
            toLocations.stream().collect(Collectors.toMap(
                Function.identity()
            ) { to -> calculateDistance(from!!, to!!) })
        })
    }

    fun initDistanceMaps(locationList: List<Location>) {
        val distanceMatrix = calculateBulkDistance(locationList, locationList)
        locationList.forEach { location -> location.distanceMap = distanceMatrix[location]!! }
    }
}