package br.magnus.vehicle.persistence

import br.magnus.vehicle.domain.entities.VehicleRoutingSolution
import org.springframework.stereotype.Service
import javax.enterprise.context.ApplicationScoped

@Service
class VehicleRoutingSolutionRepository {

    private lateinit var vehicleRoutingSolution: VehicleRoutingSolution

    fun solution(): VehicleRoutingSolution? {
        return vehicleRoutingSolution
    }

    fun update(vehicleRoutingSolution: VehicleRoutingSolution) {
        this.vehicleRoutingSolution = vehicleRoutingSolution
    }
}