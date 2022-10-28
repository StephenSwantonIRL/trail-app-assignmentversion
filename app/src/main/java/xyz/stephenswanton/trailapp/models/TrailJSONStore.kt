package xyz.stephenswanton.trailapp.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import xyz.stephenswanton.trailapp.helpers.*
import timber.log.Timber
import timber.log.Timber.i
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "trails.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<Trail>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class TrailJSONStore(private val context: Context) : TrailStore {

    var trails = mutableListOf<Trail>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<Trail> {
        logAll()
        return trails
    }

    override fun create(trail: Trail) {
        trail.id = generateRandomId()
        trails.add(trail)
        serialize()
    }


    override fun update(trail: Trail) {
        // todo
    }

    override fun findById(trailId: Long): Trail? {
        var trail: Trail? = null
        trails.forEach{ item -> if(item.id == trailId){
            trail = item
        } }
        return trail
    }

    override fun deleteMarkerById(markerId: Long){
        var trail: Trail? = null
        trails.forEach{ it.markers.forEach{item -> if(item.id == markerId){
            it.also { trail = it }
        } }}
        i(trail.toString())
        var newTrails = trails!!.filter{it.id != trail?.id} as MutableList<Trail>

        var modifiedTrail = trails.filter{it.id == trail?.id}
        if (modifiedTrail.size > 0) {
            var modifiedMarkers =
                modifiedTrail[0].markers.filter { it.id != markerId } ?: mutableListOf()
            modifiedTrail[0].markers = modifiedMarkers as MutableList<TrailMarker>
            newTrails.add(modifiedTrail[0])
            trails = newTrails
        }
        serialize()
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun deleteById(trailId: Long) {
        trails = trails!!.filter{it.id != trailId} as MutableList<Trail>
        serialize()
    }

    override fun idContainingMarker(markerId: Long): Long {
        var trail: Trail? = null
        trails.forEach{ it.markers.forEach{item -> if(item.id == markerId){
            it.also { trail = it }
        } }}
        return trail?.id ?: 0
    }


    private fun serialize() {
        val jsonString = gsonBuilder.toJson(trails, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        trails = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        trails.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}