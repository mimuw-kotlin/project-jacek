package common.example.search

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import compose.project.chesstwo.App
import org.junit.Rule
import kotlin.test.Test


class ChessTwoUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sceneNavigationTwoPlayers() {
        composeTestRule.setContent {
            App()
        }

        composeTestRule.onNodeWithContentDescription("Main Menu background").assertExists()
        composeTestRule.onNodeWithText("Start game").performClick()
        composeTestRule.onNodeWithContentDescription("Board parameters background").assertExists()
        composeTestRule.onNodeWithText("Play").performClick()
        composeTestRule.onNodeWithContentDescription("Setting up background").assertExists()
        composeTestRule.onNodeWithText("Start").performClick()
        composeTestRule.onNodeWithContentDescription("Chessboard background").assertExists()
        composeTestRule.onNodeWithText("End game").performClick()
        composeTestRule.onNodeWithContentDescription("Main Menu background").assertExists()


    }

    @Test
    fun sceneNavigationOnePlayer() {
        composeTestRule.setContent {
            App()
        }

        composeTestRule.onNodeWithContentDescription("Main Menu background").assertExists()
        composeTestRule.onNodeWithText("Play the computer").performClick()
        composeTestRule.onNodeWithContentDescription("Board parameters background").assertExists()
        composeTestRule.onNodeWithText("Play").performClick()
        composeTestRule.onNodeWithContentDescription("Setting up background").assertExists()
        composeTestRule.onNodeWithText("Start").performClick()
        composeTestRule.onNodeWithContentDescription("Chessboard background").assertExists()
        composeTestRule.onNodeWithText("End game").performClick()
        composeTestRule.onNodeWithContentDescription("Main Menu background").assertExists()


    }

    @Test
    fun sceneNavigationScenario() {
        composeTestRule.setContent {
            App()
        }

        composeTestRule.onNodeWithContentDescription("Main Menu background").assertExists()
        composeTestRule.onNodeWithText("Start game").performClick()
        composeTestRule.onNodeWithContentDescription("Board parameters background").assertExists()
        composeTestRule.onNodeWithText("Classic").performClick()
        composeTestRule.onNodeWithContentDescription("Chessboard background").assertExists()
        composeTestRule.onNodeWithText("End game").performClick()
        composeTestRule.onNodeWithContentDescription("Main Menu background").assertExists()


    }

    @Test
    fun correctClassicScenarioSetup() {
        composeTestRule.setContent {
            App()
        }

        composeTestRule.onNodeWithContentDescription("Main Menu background").assertExists()
        composeTestRule.onNodeWithText("Start game").performClick()
        composeTestRule.onNodeWithContentDescription("Board parameters background").assertExists()
        composeTestRule.onNodeWithText("Classic").performClick()
        composeTestRule.onNodeWithContentDescription("Chessboard background").assertExists()
        composeTestRule.onAllNodesWithContentDescription("Queen").assertCountEquals(2)
        composeTestRule.onAllNodesWithContentDescription("Bishop").assertCountEquals(4)
        composeTestRule.onAllNodesWithContentDescription("Knight").assertCountEquals(4)
        composeTestRule.onAllNodesWithContentDescription("Rook").assertCountEquals(4)
        composeTestRule.onAllNodesWithContentDescription("Pawn").assertCountEquals(16)
    }

    @Test
    fun correctSandomierzGambitScenarioSetup() {
        composeTestRule.setContent {
            App()
        }

        composeTestRule.onNodeWithContentDescription("Main Menu background").assertExists()
        composeTestRule.onNodeWithText("Start game").performClick()
        composeTestRule.onNodeWithContentDescription("Board parameters background").assertExists()
        composeTestRule.onNodeWithText("Sandomierz Gambit").performClick()
        composeTestRule.onNodeWithContentDescription("Chessboard background").assertExists()
        composeTestRule.onAllNodesWithContentDescription("Queen").assertCountEquals(2)
        composeTestRule.onAllNodesWithContentDescription("Bishop").assertCountEquals(3)
        composeTestRule.onAllNodesWithContentDescription("Knight").assertCountEquals(3)
        composeTestRule.onAllNodesWithContentDescription("Rook").assertCountEquals(3)
        composeTestRule.onAllNodesWithContentDescription("Pawn").assertCountEquals(16)
    }

    @Test
    fun correctRohanVsMordorScenarioSetup() {
        composeTestRule.setContent {
            App()
        }

        composeTestRule.onNodeWithContentDescription("Main Menu background").assertExists()
        composeTestRule.onNodeWithText("Start game").performClick()
        composeTestRule.onNodeWithContentDescription("Board parameters background").assertExists()
        composeTestRule.onNodeWithText("Rohan vs Mordor").performClick()
        composeTestRule.onNodeWithContentDescription("Chessboard background").assertExists()
        composeTestRule.onAllNodesWithContentDescription("Knight").assertCountEquals(23)
        composeTestRule.onAllNodesWithContentDescription("Rook").assertCountEquals(2)
        composeTestRule.onAllNodesWithContentDescription("Pawn").assertCountEquals(36)
    }

    @Test
    fun mainMenuButtons() {
        composeTestRule.setContent {
            App()
        }
        composeTestRule.onNodeWithText("Start game").assertExists()
        composeTestRule.onNodeWithText("Play the computer").assertExists()
        composeTestRule.onNodeWithText("Exit").assertExists()
    }

    @Test
    fun boardParametersButtons() {
        composeTestRule.setContent {
            App()
        }
        composeTestRule.onNodeWithText("Start game").performClick()
        composeTestRule.onNodeWithText("Play").assertExists()
        composeTestRule.onNodeWithText("Classic").assertExists()
        composeTestRule.onNodeWithText("Sandomierz Gambit").assertExists()
        composeTestRule.onNodeWithText("Rohan vs Mordor").assertExists()
    }

    @Test
    fun settingUpButtons() {
        composeTestRule.setContent {
            App()
        }
        composeTestRule.onNodeWithText("Start game").performClick()
        composeTestRule.onNodeWithText("Play").performClick()
        composeTestRule.onNodeWithText("Black Side").assertExists()
        composeTestRule.onNodeWithText("Black Side").performClick()
        composeTestRule.onNodeWithText("White Side").assertExists()
        composeTestRule.onNodeWithText("Start").assertExists()
        composeTestRule.onNodeWithText("Main Menu").assertExists()
    }

    @Test
    fun chessboardButtons() {
        composeTestRule.setContent {
            App()
        }
        composeTestRule.onNodeWithText("Start game").performClick()
        composeTestRule.onNodeWithText("Play").performClick()
        composeTestRule.onNodeWithText("Start").performClick()
        composeTestRule.onNodeWithText("End game").assertExists()
    }
}
