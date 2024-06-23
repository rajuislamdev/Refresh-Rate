package com.example.refresh_tracker

import android.view.Choreographer
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink

class MainActivity : FlutterActivity() {
    private val eventChannel = "com.example.refresh_tracker/refresh_rate"
    private var lastFrameTimeNanos: Long = 0
    private var eventSink: EventChannel.EventSink? = null
    private var frameCount: Int = 0
    private var startTimeNanos: Long = 0

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Setup EventChannel for refresh rate
        EventChannel(flutterEngine.dartExecutor.binaryMessenger, eventChannel).setStreamHandler(
            object : EventChannel.StreamHandler {
                override fun onListen(arguments: Any?, events: EventSink?) {
                    eventSink = events
                    startTracking()
                }

                override fun onCancel(arguments: Any?) {
                    stopTracking()
                }
            }
        )
    }

    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (lastFrameTimeNanos != 0L) {
                val elapsedNanos = frameTimeNanos - lastFrameTimeNanos
                val refreshRate = 1_000_000_000f / elapsedNanos
                frameCount++
                val elapsedTime = frameTimeNanos - startTimeNanos
                val fps = if (elapsedTime > 0) (frameCount * 1_000_000_000L / elapsedTime) else 0
                val data = mapOf(
                    "refreshRate" to refreshRate,
                    "fps" to fps.toFloat()
                )
                eventSink?.success(data)
            } else {
                startTimeNanos = frameTimeNanos
                frameCount = 1 // Start from the first frame
            }
            lastFrameTimeNanos = frameTimeNanos
            Choreographer.getInstance().postFrameCallback(this)
        }
    }

    private fun startTracking() {
        frameCount = 0
        startTimeNanos = 0
        lastFrameTimeNanos = 0
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }

    private fun stopTracking() {
        Choreographer.getInstance().removeFrameCallback(frameCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTracking()
    }
}
