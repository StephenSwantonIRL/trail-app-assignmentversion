package xyz.stephenswanton.trailapp.models

interface TrailStore {
    fun findAll(): List<Trail>
    fun create(trail: Trail)
    fun update(trail: Trail)
}