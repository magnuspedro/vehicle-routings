package br.magnus.vehicle.domain.converter

import br.magnus.vehicle.domain.entities.*
import br.magnus.vehicle.domain.geo.DistanceCalculator
import br.magnus.vehicle.domain.geo.EuclideanDistanceCalculator
import br.magnus.vehicle.persistence.VehicleRoutingSolutionRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.random.Random

@Service
class ConvertToSolution(
    private val repository: VehicleRoutingSolutionRepository,
) {

    private val distanceCalculator: DistanceCalculator = EuclideanDistanceCalculator()

    fun convertAndSovle(message: Triple<List<Customer>, List<Depot>, List<Vehicle>>) {
        val (customerList, depotList, vehicleList) = message
        val locations = Stream.concat(customerList.stream().map { customer -> customer.location },
            depotList.stream().map { depot -> depot.location }).collect(Collectors.toList())
        updateVehicleLocation(vehicleList, depotList)

        distanceCalculator.initDistanceMaps(locations)

        val vehicleRoutingSolution = VehicleRoutingSolution(
            name = "VehicleRouting",
            locationsList = locations,
            depotList = depotList,
            customerList = customerList,
            vehicleList = vehicleList,
            northEastCorner = Location(0, 3.1869082, -52.2150111),
            southWestCorner = Location(0, -33.6859472, -53.4654995)
        )

        repository.update(vehicleRoutingSolution)
    }

    private fun updateVehicleLocation(vehicleList: List<Vehicle>, depotList: List<Depot>) {
        vehicleList.map { it.depot = depotList[Random.nextInt(0, depotList.size)] }
    }
}