package br.magnus.readeble.gateway.database

import br.magnus.readeble.domain.entities.VehicleData
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class VehicleServices {
    @Autowired
    lateinit var mongoClient: MongoClient

    fun findAll(): List<VehicleData> {
        return getCollection().find().map {
            VehicleData(
                plate = it.getString("plate"),
                capacity = it.getString("capacity"),
                type = it.getString("type"),
                consumption = it.getString("consumption")
            )
        }.toSet().toList()
    }

    private fun getCollection(): MongoCollection<Document> {
        return mongoClient.getDatabase("route").getCollection("vehicle")
    }
}