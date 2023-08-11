package com.danosoftware.macrobenchmark.test

import android.content.Intent
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val PACKAGE = "com.danosoftware.galaxyforce"
private const val GAME_WIDTH = 540
private const val GAME_HEIGHT = 960

@RunWith(AndroidJUnit4::class)
class AppBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = PACKAGE,
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        val intent = Intent()
        intent.setPackage(PACKAGE)
        intent.action = "$PACKAGE.BENCHMARK"
        startActivityAndWait(intent)
    }

    @Test
    fun timing() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)
        benchmarkRule.measureRepeated(
            packageName = PACKAGE,
            metrics = listOf(FrameTimingMetric()),
            iterations = 5,
            startupMode = StartupMode.WARM
        ) {
            pressHome()
            val intent = Intent()
            intent.setPackage(PACKAGE)
            intent.action = "$PACKAGE.BENCHMARK"
            startActivityAndWait(intent)

            // wait for splash screen to transition to main menu
            wait(device, 10000)
            device.waitForIdle()

            // turn on options
            click(device, GAME_WIDTH / 2, (2 * 170) + 100)
            wait(device, 1000)
            click(device, 180, (2 * 170) + 100)
            wait(device, 500)
            click(device, 180, (3 * 170) + 100)
            wait(device, 500)
            click(device, 180, (4 * 170) + 100)
            wait(device, 500)

            // display achievements
            click(device, GAME_WIDTH / 2, (1 * 170) + 100)
            wait(device, 5000)
            device.pressBack()
            wait(device, 1000)

            // return to main menu
            device.pressBack()
            device.waitForIdle()
            wait(device, 1000)

            // click on "play" button
            click(device, GAME_WIDTH / 2, (3 * 170) + 100)
            wait(device, 1000)

            // click left arrow until reach zone 1
            click(device, 100, 100 + (4 * 170))
            wait(device, 1000)
            click(device, 100, 100 + (4 * 170))
            wait(device, 1000)
            click(device, 100, 100 + (4 * 170))
            wait(device, 1000)
            click(device, 100, 100 + (4 * 170))
            wait(device, 1000)

            // click on wave 1 start
            click(device, 100, 100 + (3 * 170))
            wait(device, 5000)

            // move to top of screen
            swipe(device, GAME_WIDTH / 2, GAME_HEIGHT - 200, GAME_WIDTH / 2, GAME_HEIGHT - 200)
            swipe(device, GAME_WIDTH - 100, 100, GAME_WIDTH - 100, 100)
            swipe(device, 100, 100, 100, 100)
            swipe(device, GAME_WIDTH / 2, GAME_HEIGHT - 200, GAME_WIDTH / 2, GAME_HEIGHT - 200)
            swipe(device, GAME_WIDTH / 2, 200, GAME_WIDTH / 2, 200)
            device.waitForIdle()

            // leave game
            device.pressBack()
            device.waitForIdle()
            wait(device, 1000)

            // return to wave chooser
            device.pressBack()
            device.waitForIdle()
            wait(device, 1000)

            // return to main menu
            device.pressBack()
            device.waitForIdle()
            wait(device, 1000)

            // exit game
            device.pressBack()
            device.waitForIdle()
        }
    }

    // wait for supplied time in milliseconds on supplied device
    // UiDevice does not support unconditional wait so we wait for an object that will never appear
    fun wait(device: UiDevice, timeDelayMs: Long) = device.wait(
        Until.findObject(By.text("foo")),
        timeDelayMs
    )

    // click point on device screen
    fun click(device: UiDevice, x: Int, y: Int) =
        device.click(
            xPos(device, x),
            yPos(device, y)
        )

    fun swipe(device: UiDevice, x1: Int, y1: Int, x2: Int, y2: Int) =
        device.swipe(
            xPos(device, x1),
            yPos(device, y1),
            xPos(device, x2),
            yPos(device, y2),
            100
        )

    // convert x-position on screen to x-position on device
    fun xPos(device: UiDevice, x: Int) =
        ((device.displayWidth.toDouble() / GAME_WIDTH) * x).toInt()

    // convert y-position on screen to y-position on device
    fun yPos(device: UiDevice, y: Int) =
        ((device.displayHeight.toDouble() / GAME_HEIGHT) * (GAME_HEIGHT - y)).toInt() + 242

}