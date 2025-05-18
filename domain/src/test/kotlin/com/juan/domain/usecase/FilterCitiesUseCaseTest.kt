package com.juan.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.juan.domain.model.City
import com.juan.domain.model.City.GeoCoordinates
import kotlin.test.Test

class FilterCitiesUseCaseTest {

    private val useCase = FilterCitiesUseCase()

    private val cities = listOf(
        City(1, "Berlin", "DE", GeoCoordinates(0.0, 0.0), "", true),
        City(2, "Barcelona", "ES", GeoCoordinates(0.0, 0.0), "", false),
        City(3, "Boston", "US", GeoCoordinates(0.0, 0.0), "", true),
        City(4, "Buenos Aires", "AR", GeoCoordinates(0.0, 0.0), "", false),
        City(5, "bordeaux", "FR", GeoCoordinates(0.0, 0.0), "", true),
        City(6, "Amsterdam", "NL", GeoCoordinates(0.0, 0.0), "", false),
        City(7, "Berlin", "US", GeoCoordinates(0.0, 0.0), "", false),
        City(8, "Beijing", "CN", GeoCoordinates(0.0, 0.0), "", true),
        City(9, "Lubeck", "DE", GeoCoordinates(0.0, 0.0), "", false),
        City(10, "Belgrade", "RS", GeoCoordinates(0.0, 0.0), "", false),
    )

    @Test
    fun `filters cities with prefix B and only favorites`() {
        val result = useCase(cities, prefix = "B", onlyFavorites = true)
        assertThat(result.map { it.name }).containsExactly("Beijing", "Berlin", "Boston")
    }

    @Test
    fun `filters cities with prefix B and includes non-favorites`() {
        val result = useCase(cities, prefix = "B", onlyFavorites = false)
        assertThat(result.map { it.name }).containsExactly("Barcelona", "Beijing", "Belgrade", "Berlin", "Berlin", "Boston", "Buenos Aires")
    }

    @Test
    fun `returns empty list for case-sensitive mismatch`() {
        val result = useCase(cities, prefix = "b", onlyFavorites = false)
        assertThat(result.map { it.name }).containsExactly("bordeaux")
    }

    @Test
    fun `filters cities case-sensitive prefix only`() {
        val result = useCase(cities, prefix = "Be", onlyFavorites = false)
        assertThat(result.map { it.name }).containsExactly("Beijing", "Belgrade", "Berlin", "Berlin")
    }

    @Test
    fun `ignores cities that do not start with prefix`() {
        val result = useCase(cities, prefix = "Lu", onlyFavorites = false)
        assertThat(result.map { it.name }).containsExactly("Lubeck")
    }

    @Test
    fun `returns all matching cities when prefix is empty`() {
        val result = useCase(cities, prefix = "", onlyFavorites = false)
        assertThat(result).hasSize(10)
    }

    @Test
    fun `filters only favorites when prefix is empty`() {
        val result = useCase(cities, prefix = "", onlyFavorites = true)
        assertThat(result.map { it.name }).containsExactly("Beijing", "Berlin", "Boston", "bordeaux")
    }

    @Test
    fun `sorts by name then country`() {
        val result = useCase(cities, prefix = "Berlin", onlyFavorites = false)
        assertThat(result.map { it.country }).isEqualTo(listOf("DE", "US"))
    }

    @Test
    fun `filters nothing if prefix has no match`() {
        val result = useCase(cities, prefix = "Z", onlyFavorites = false)
        assertThat(result).isEmpty()
    }

    @Test
    fun `filters cities with multi-word prefix`() {
        val result = useCase(cities, prefix = "Buenos", onlyFavorites = false)
        assertThat(result.map { it.name }).containsExactly("Buenos Aires")
    }
}