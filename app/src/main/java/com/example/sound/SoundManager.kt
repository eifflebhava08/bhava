package com.example.sound

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

object SoundManager {

    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Plays a cute pop click sound when applying a makeup item.
     */
    fun playClickSound() {
        scope.launch {
            generateTone(freqHz = 880f, durationMs = 60, volume = 0.4f, isPop = true)
        }
    }

    /**
     * Plays a sparkling chime melody for Random Look or Sparkles!
     */
    fun playSparkleChime() {
        scope.launch {
            val notes = listOf(523.25f, 659.25f, 783.99f, 1046.50f, 1318.51f) // C5, E5, G5, C6, E6
            notes.forEachIndexed { index, freq ->
                generateTone(freqHz = freq, durationMs = 70, volume = 0.3f)
                kotlinx.coroutines.delay(40)
            }
        }
    }

    /**
     * Plays an undo swoosh sound.
     */
    fun playUndoSound() {
        scope.launch {
            val notes = listOf(659.25f, 523.25f, 392.00f) // E5, C5, G4
            notes.forEach { freq ->
                generateTone(freqHz = freq, durationMs = 60, volume = 0.3f)
                kotlinx.coroutines.delay(35)
            }
        }
    }

    /**
     * Plays a celebratory victory fanfare when look scoring completes!
     */
    fun playFanfareSound() {
        scope.launch {
            val notes = listOf(523.25f, 659.25f, 783.99f, 1046.50f, 880.0f, 1046.50f)
            val durations = listOf(90L, 90L, 90L, 150L, 100L, 300L)
            notes.forEachIndexed { i, freq ->
                generateTone(freqHz = freq, durationMs = durations[i].toInt(), volume = 0.5f)
                kotlinx.coroutines.delay(durations[i] + 20)
            }
        }
    }

    private fun generateTone(
        freqHz: Float,
        durationMs: Int,
        volume: Float = 0.3f,
        isPop: Boolean = false
    ) {
        try {
            val sampleRate = 22050
            val numSamples = (durationMs * sampleRate) / 1000
            val samples = ShortArray(numSamples)

            val twopi = 2.0 * Math.PI
            var currentFreq = freqHz.toDouble()

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                if (isPop) {
                    currentFreq = freqHz * (1.0 - (i.toDouble() / numSamples) * 0.5)
                }

                // Smooth envelope (fade in & fade out)
                val envelope = when {
                    i < numSamples * 0.1 -> i / (numSamples * 0.1)
                    i > numSamples * 0.7 -> (numSamples - i) / (numSamples * 0.3)
                    else -> 1.0
                }

                val value = (sin(twopi * currentFreq * t) * envelope * volume * 32767).toInt()
                samples[i] = value.coerceIn(-32768, 32767).toShort()
            }

            val bufferSize = numSamples * 2
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(samples, 0, numSamples)
            audioTrack.play()
            // Clean up audio track after playback
            scope.launch {
                kotlinx.coroutines.delay(durationMs.toLong() + 100)
                try {
                    audioTrack.stop()
                    audioTrack.release()
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {
            // Audio synthesize fallback gracefully
        }
    }
}
