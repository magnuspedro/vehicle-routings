package br.magnus.vehicle.bootstrap

import br.magnus.vehicle.domain.entities.*
import br.magnus.vehicle.domain.geo.DistanceCalculator
import br.magnus.vehicle.domain.geo.EuclideanDistanceCalculator
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import java.util.function.Supplier
import java.util.stream.Collectors
import java.util.stream.Stream

class DemoDataBuilder {
    companion object {
        private val sequence: AtomicLong = AtomicLong()

        fun builder(): DemoDataBuilder {
            return DemoDataBuilder()
        }
    }

    private val distanceCalculator: DistanceCalculator = EuclideanDistanceCalculator();
    private var southWestCorner: Location? = null
    private var northEastCorner: Location? = null
    private var customerCount: Int = 0
    private var vehicleCount: Int = 0
    private var depotCount: Int = 0
    private var minDemand: Int = 0
    private var maxDemand: Int = 0
    private var vehicleCapacity: Int = 0


    fun setSouthWestCorner(southWestCorner: Location?): DemoDataBuilder {
        this.southWestCorner = southWestCorner
        return this
    }

    fun setNorthEastCorner(northEastCorner: Location?): DemoDataBuilder {
        this.northEastCorner = northEastCorner
        return this
    }

    fun setMinDemand(minDemand: Int): DemoDataBuilder {
        this.minDemand = minDemand
        return this
    }

    fun setMaxDemand(maxDemand: Int): DemoDataBuilder {
        this.maxDemand = maxDemand
        return this
    }

    fun setCustomerCount(customerCount: Int): DemoDataBuilder {
        this.customerCount = customerCount
        return this
    }

    fun setVehicleCount(vehicleCount: Int): DemoDataBuilder {
        this.vehicleCount = vehicleCount
        return this
    }

    fun setDepotCount(depotCount: Int): DemoDataBuilder {
        this.depotCount = depotCount
        return this
    }

    fun setVehicleCapacity(vehicleCapacity: Int): DemoDataBuilder {
        this.vehicleCapacity = vehicleCapacity
        return this
    }

    fun build(): VehicleRoutingSolution {
        if (minDemand < 1) {
            throw IllegalStateException("minDemand ($minDemand) must be greater than zero.")
        }
        if (maxDemand < 1) {
            throw IllegalStateException("maxDemand ($maxDemand) must be greater than zero.")
        }
        if (minDemand >= maxDemand) {
            throw IllegalStateException(
                "maxDemand ($maxDemand ) must be greater than minDemand ($minDemand)."
            )
        }
        if (vehicleCapacity < 1) {
            throw IllegalStateException(
                "Number of vehicleCapacity ($vehicleCapacity) must be greater than zero."
            )
        }
        if (customerCount < 1) {
            throw IllegalStateException(
                "Number of customerCount ($customerCount) must be greater than zero."
            )
        }
        if (vehicleCount < 1) {
            throw IllegalStateException(
                "Number of vehicleCount ($vehicleCount) must be greater than zero."
            )
        }
        if (depotCount < 1) {
            throw IllegalStateException(
                "Number of depotCount ($depotCount) must be greater than zero."
            )
        }

        if (northEastCorner?.latitude!! <= southWestCorner?.latitude!!) {
            throw IllegalStateException(
                "northEastCorner.getLatitude (${northEastCorner?.latitude.toString()}) must be greater" +
                        " than southWestCorner.getLatitude(${southWestCorner?.latitude.toString()})."
            )
        }

        if (northEastCorner?.longitude!! <= southWestCorner?.longitude!!) {
            throw IllegalStateException(
                ("northEastCorner.getLongitude (${northEastCorner?.longitude.toString()}) must be greater " +
                        "than southWestCorner.getLongitude(${southWestCorner?.longitude.toString()}).")
            )
        }

        val name = "demo"

        val random = Random(0)
        val latitudes = random
            .doubles(southWestCorner?.latitude!!, northEastCorner?.latitude!!).iterator()
        val longitudes = random
            .doubles(southWestCorner?.longitude!!, northEastCorner?.longitude!!).iterator()

        val demand = random.ints(minDemand, maxDemand + 1).iterator()
        val depotRandom = random.ints(0, depotCount).iterator()

        val depotSupplier: Supplier<Depot> = Supplier {
            Depot(
                sequence.incrementAndGet(),
                Location(sequence.incrementAndGet(), latitudes.nextDouble(), longitudes.nextDouble())
            )
        }
        val depotList: List<Depot> = Stream.generate(depotSupplier)
            .limit(depotCount.toLong())
            .collect(Collectors.toList())

        val vehicleSupplier: Supplier<Vehicle> = Supplier {
            Vehicle(
                id = sequence.incrementAndGet(),
                capacity = vehicleCapacity.toLong(),
                depot = depotList[depotRandom.nextInt()]
            )
        }

        val vehicleList: List<Vehicle> = Stream.generate(vehicleSupplier)
            .limit(vehicleCount.toLong())
            .collect(Collectors.toList())

        val customerSupplier: Supplier<Customer> = Supplier {
            Customer(
                sequence.incrementAndGet(),
                Location(sequence.incrementAndGet(), latitudes.nextDouble(), longitudes.nextDouble()),
                demand.nextInt().toLong()
            )
        }
        val customerList: List<Customer> = Stream.generate(customerSupplier)
            .limit(customerCount.toLong())
            .collect(Collectors.toList())

        val locationsList: List<Location> = Stream.concat(
            customerList.stream().map { customer -> customer.location },
            depotList.stream().map { depot -> depot.location })
            .collect(Collectors.toList());

        distanceCalculator.initDistanceMaps(locationsList)

        return VehicleRoutingSolution(
            name = name,
            locationsList = locationsList,
            depotList = depotList,
            vehicleList = vehicleList,
            customerList = customerList,
            southWestCorner = southWestCorner,
            northEastCorner = northEastCorner
        )
    }

}