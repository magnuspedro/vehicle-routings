package br.magnus.vehicle.domain.entities

import br.magnus.vehicle.bootstrap.DemoDataBuilder
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty
import org.optaplanner.core.api.domain.solution.PlanningScore
import org.optaplanner.core.api.domain.solution.PlanningSolution
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore

@PlanningSolution
data class VehicleRoutingSolution(
    var name: String? = null,
    @ProblemFactCollectionProperty
    var locationsList: List<Location>? = emptyList(),
    @ProblemFactCollectionProperty
    var depotList: List<Depot>? = emptyList(),
    @PlanningEntityCollectionProperty
    var vehicleList: List<Vehicle>? = emptyList(),
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "customerRange")
    var customerList: List<Customer>? = emptyList(),
    @PlanningScore
    var score: HardSoftLongScore? = null,
    var southWestCorner: Location? = null,
    var northEastCorner: Location? = null
) {
    fun getBounds(): List<Location?> {
        return listOf(southWestCorner, northEastCorner)
    }

    fun getDistanceMeters(): Long {
        return if (score == null) 0 else -score?.softScore!!
    }

    companion object {
        fun empty(): VehicleRoutingSolution {
            val problem: VehicleRoutingSolution = DemoDataBuilder.builder().setMinDemand(1).setMaxDemand(2)
                .setVehicleCapacity(77).setCustomerCount(77).setVehicleCount(7).setDepotCount(1)
                .setSouthWestCorner(Location(0, 5.44, -0.6))
                .setNorthEastCorner(Location(0, 5.56, -0.0)).build()
            problem.score = HardSoftLongScore.ZERO

            return problem
        }
    }
}