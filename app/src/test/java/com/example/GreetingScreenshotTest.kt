package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.House
import com.example.ui.components.HouseCard
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val sampleHouse = House(
      title = "২ রুমের ফ্ল্যাট ভাড়া",
      area = "চৌরাস্তা, গাজীপুর",
      rent = "৮,০০০ টাকা/মাস",
      phone = "01700000000",
      image = "house_flat_1",
      houseType = "ফ্যামিলি ফ্ল্যাট",
      bedrooms = "২ রুম"
    )

    composeTestRule.setContent {
      MyApplicationTheme {
        HouseCard(
          house = sampleHouse,
          onCardClick = {},
          onCallClick = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
