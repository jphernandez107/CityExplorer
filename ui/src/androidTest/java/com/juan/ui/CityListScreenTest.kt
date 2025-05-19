package com.juan.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.juan.ui.citylist.CityItemViewState
import com.juan.ui.citylist.CityListUiEvent
import com.juan.ui.citylist.CityListViewState
import com.juan.ui.citylist.FavoriteState
import com.juan.ui.citylist.SearchBarViewState
import com.juan.ui.citylist.screen.CityItemCard
import com.juan.ui.citylist.screen.CityListScreenContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CityListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleCities = listOf(
        CityItemViewState(
            id = 1,
            name = "Berlin",
            country = "DE",
            coordinates = "52.52, 13.405",
            favoriteState = FavoriteState.NotFavorite
        ),
        CityItemViewState(
            id = 2,
            name = "Boston",
            country = "US",
            coordinates = "42.3601, -71.0589",
            favoriteState = FavoriteState.Favorite
        )
    )

    @Test
    fun cityList_displays_all_cities() {
        composeTestRule.setContent {
            CityListScreenContent(
                viewState = CityListViewState.Success(cities = sampleCities),
                searchBarViewState = SearchBarViewState(""),
                onEvent = {},
            )
        }

        composeTestRule.onNodeWithText("Berlin, DE").assertExists()
        composeTestRule.onNodeWithText("Boston, US").assertExists()
    }

    @Test
    fun cityList_shows_empty_message_when_no_results() {
        composeTestRule.setContent {
            CityListScreenContent(
                viewState = CityListViewState.Empty,
                searchBarViewState = SearchBarViewState("Z"),
                onEvent = {},
            )
        }

        composeTestRule.onNodeWithText("No cities match your search.").assertExists()
    }

    @Test
    fun clicking_city_row_triggers_event() {
        var clickedId: Long? = null

        composeTestRule.setContent {
            CityListScreenContent(
                viewState = CityListViewState.Success(sampleCities),
                searchBarViewState = SearchBarViewState(""),
                onEvent = {
                    if (it is CityListUiEvent.OnCityClick) clickedId = it.cityId
                }
            )
        }

        composeTestRule.onNodeWithText("Berlin, DE").performClick()
        assert(clickedId == 1L)
    }

    @Test
    fun cityCard_displays_name_country_coordinates() {
        val city = CityItemViewState(
            id = 1,
            name = "Berlin",
            country = "DE",
            coordinates = "52.52, 13.405",
            favoriteState = FavoriteState.Favorite
        )

        composeTestRule.setContent {
            CityItemCard(
                city = city,
                onRowClick = {},
                onFavoriteToggle = {},
            )
        }

        composeTestRule.onNodeWithText("Berlin, DE").assertExists()
        composeTestRule.onNodeWithText("52.52, 13.405").assertExists()
    }

    @Test
    fun cityCard_displays_favorite_icon_state() {
        val city = CityItemViewState(
            id = 1,
            name = "Berlin",
            country = "DE",
            coordinates = "52.52, 13.405",
            favoriteState = FavoriteState.Favorite
        )

        composeTestRule.setContent {
            CityItemCard(
                city = city,
                onRowClick = {},
                onFavoriteToggle = {},
            )
        }

        composeTestRule.onNodeWithTag("FavoriteIcon_1").assertExists()
    }
}