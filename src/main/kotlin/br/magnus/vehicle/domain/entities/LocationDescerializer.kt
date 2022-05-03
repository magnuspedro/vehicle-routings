package br.magnus.vehicle.domain.entities

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class LocationDescerializer : KeyDeserializer() {

    private val mapper = jacksonObjectMapper()

    override fun deserializeKey(key: String?, ctx: DeserializationContext?): Location {
        return mapper.readValue(key!!)
    }
}