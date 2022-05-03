package br.magnus.vehicle.persistence.converter

import br.magnus.readeble.gateway.database.VehicleServices
import br.magnus.vehicle.domain.entities.Customer
import br.magnus.vehicle.domain.entities.Depot
import br.magnus.vehicle.domain.entities.Location
import br.magnus.vehicle.domain.entities.Vehicle
import br.magnus.vehicle.persistence.DestinationServices
import br.magnus.vehicle.persistence.OriginServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random

@Component
class ConvertFromDb {

    @Autowired
    private lateinit var originServices: OriginServices

    @Autowired
    private lateinit var vehicleServices: VehicleServices

    @Autowired
    private lateinit var destinationServices: DestinationServices
    private val sequence: AtomicLong = AtomicLong()

    fun convert(): Triple<List<Customer>, List<Depot>, List<Vehicle>> {
        val (customerList, depotList) = generateFromRoutes()
        val vehicleList = generateFromVehicles(depotList)

        return Triple(customerList, depotList, vehicleList)
    }

    private fun generateFromRoutes(): Pair<List<Customer>, List<Depot>> {
        val routes = originServices.findAll()
        val destination = destinationServices.findAll()

        val customerList: MutableSet<Customer> = emptySet<Customer>().toMutableSet()
        val depotList: MutableSet<Depot> = emptySet<Depot>().toMutableSet()
        destination.forEach {
            customerList.add(
                Customer(
                    id = sequence.getAndIncrement(),
                    name = it.destination,
                    location = Location(
                        id = sequence.getAndIncrement(),
                        latitude = it.destinationLatitude!!,
                        longitude = it.destinationLongitude!!
                    ),
                    demand = it.weight!!
                )
            )
        }
        routes.forEach {
            depotList.add(
                Depot(
                    id = sequence.getAndIncrement(),
                    name = it.origin,
                    location = Location(
                        id = sequence.getAndIncrement(),
                        latitude = it.originLatitude!!,
                        longitude = it.originLongitude!!
                    )
                )
            )
        }
        return Pair(customerList.toList(), depotList.toList())
    }

    private fun generateFromVehicles(depotList: List<Depot>): List<Vehicle> {
        val vehicles = vehicleServices.findAll()

        val vehicleList: MutableSet<Vehicle> = emptySet<Vehicle>().toMutableSet()

        vehicles.forEach {
            vehicleList.add(
                Vehicle(
                    id = sequence.getAndIncrement(),
                    plate = it.plate,
                    capacity = it.capacity!!.replace("[^\\d.]".toRegex(), "").toLong(),
                    depot = depotList[Random.nextInt(0, depotList.size)],
                    consumption = it.consumption
                )
            )
        }
        return vehicleList.toList()
    }
}
