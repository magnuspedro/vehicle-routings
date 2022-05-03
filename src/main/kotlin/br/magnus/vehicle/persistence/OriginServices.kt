package br.magnus.vehicle.persistence

import br.magnus.vehicle.domain.entities.database.Origin
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class OriginServices {
    @Autowired
    lateinit var mongoClient: MongoClient

    fun findAll(): List<Origin> {
        return getCollection().find().map {
            Origin(
                origin = it.getString("origin"),
                originState = it.getString("originState"),
                originLatitude = it.getDouble("originLatitude"),
                originLongitude = it.getDouble("originLongitude"),
            )
        }.toSet().toList()

    }

    private fun getCollection(): MongoCollection<Document> {
        return mongoClient.getDatabase("route").getCollection("origin")
    }
}