package br.magnus.vehicle.domain.solver

import br.magnus.vehicle.domain.entities.Customer
import br.magnus.vehicle.domain.entities.Depot
import br.magnus.vehicle.domain.entities.VehicleRoutingSolution
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory
import java.util.Comparator.comparingDouble
import java.util.Comparator.comparingLong

class DepotAngleCustomerDifficultWeightFactory : SelectionSorterWeightFactory<VehicleRoutingSolution, Customer> {
    override fun createSorterWeight(
        vehicleRoutingSolution: VehicleRoutingSolution,
        customer: Customer
    ): DepotAngleCustomerDifficultyWeight? {
        val depot: Depot? = vehicleRoutingSolution.depotList?.get(0)
        return DepotAngleCustomerDifficultyWeight(
            customer,
            customer.location.getAngle(depot?.location!!),
            customer.location.getDistanceTo(depot.location)!!
                    + depot.location.getDistanceTo(customer.location)!!
        )
    }

    companion object {
        class DepotAngleCustomerDifficultyWeight(
            private val customer: Customer,
            private val depotAngle: Double,
            private val depotRoundTripDistance: Long
        ) : Comparable<DepotAngleCustomerDifficultyWeight> {
            private val COMPARATOR: Comparator<DepotAngleCustomerDifficultyWeight> =
                comparingDouble { weight: DepotAngleCustomerDifficultyWeight -> weight.depotAngle }
                    .thenComparingLong { weight -> weight.depotRoundTripDistance }
                    .thenComparing({ weight -> weight.customer }, comparingLong { customer -> customer.id })

            override fun compareTo(other: DepotAngleCustomerDifficultyWeight): Int {
                return COMPARATOR.compare(this, other)
            }
        }
    }
}