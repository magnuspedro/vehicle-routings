package br.magnus.vehicle.persistence

import br.magnus.vehicle.domain.entities.database.Destination
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class DestinationServices {
    @Autowired
    lateinit var mongoClient: MongoClient

    fun findAll(): List<Destination> {
        return getCollection().find().map {
            Destination(
                destination = it.getString("destination"),
                destinationState = it.getString("destinationState"),
                destinationLatitude = it.getDouble("destinationLatitude"),
                destinationLongitude = it.getDouble("destinationLongitude"),
                weight = it.getDouble("weight").toLong(),
                loadType = it.getString("loadType")
            )
        }.toSet().toList()

    }

    private fun getCollection(): MongoCollection<Document> {
        return mongoClient.getDatabase("route").getCollection("destination")
    }
}
