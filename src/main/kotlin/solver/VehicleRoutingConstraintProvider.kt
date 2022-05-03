package solver

import br.magnus.vehicle.domain.entities.Vehicle
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore
import org.optaplanner.core.api.score.stream.Constraint
import org.optaplanner.core.api.score.stream.ConstraintFactory
import org.optaplanner.core.api.score.stream.ConstraintProvider

class VehicleRoutingConstraintProvider : ConstraintProvider {
    override fun defineConstraints(factory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
            vehicleCapacity(factory),
            totalDistance(factory)
        )
    }

    protected fun vehicleCapacity(factory: ConstraintFactory): Constraint {
        return factory.forEach(Vehicle::class.java)
            .filter { vehicle -> vehicle.getTotalDemand() > vehicle?.capacity!! }
            .penalizeLong(
                "vehicleCapacity",
                HardSoftLongScore.ONE_HARD
            ) { vehicle -> vehicle.getTotalDemand() - vehicle?.capacity!! }
    }

    protected fun totalDistance(factory: ConstraintFactory): Constraint {
        return factory.forEach(Vehicle::class.java)
            .penalizeLong(
                "distanceFromPreviousStandstill",
                HardSoftLongScore.ONE_SOFT
            ) { vehicle -> vehicle.getTotalDistanceMeters() }
    }
}