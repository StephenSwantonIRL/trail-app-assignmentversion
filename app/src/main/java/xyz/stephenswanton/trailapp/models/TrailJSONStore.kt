package xyz.stephenswanton.trailapp.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import xyz.stephenswanton.trailapp.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "placemarks.json"
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