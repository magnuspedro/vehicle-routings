package br.magnus.vehicle.domain.entities

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.variable.PlanningListVariable

@PlanningEntity
data class Vehicle(
    var id: Long? = null,
    var capacity: Long? = null,
    var depot: Depot? = null,
    var plate: String? = null,
    var consumption: String? = null,

    @PlanningListVariable(valueRangeProviderRefs = ["customerRange"])
    var customerList: List<Customer>? = emptyList()
) {
    fun getRoute(): List<Location> {
        if (customerList?.isEmpty() == true) {
            return emptyList()
        }

        val route: MutableList<Location> = mutableListOf(depot?.location!!)
        for (customer in customerList!!) {
            route.add(customer.location)
        }
        route.add(depot?.location!!)

        return route
    }

    fun getTotalDemand(): Long {
        var totalDemand: Long = 0L
        for (customer in customerList!!) {
            totalDemand += customer.demand
        }
        return totalDemand
    }

    fun getTotalDistanceMeters(): Long {
        if (customerList?.isEmpty() == true) {
            return 0
        }

        var totalDistance: Long = 0
        var previousLocation: Location = depot?.location!!

        for (customer in customerList!!) {
            totalDistance += previousLocation.getDistanceTo(customer.location)!!
            previousLocation = customer.location
        }
        totalDistance += previousLocation.getDistanceTo(depot?.location!!)!!

        return totalDistance
    }
}
