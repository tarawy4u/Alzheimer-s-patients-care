package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Native, offline therapeutic audio synthesizer designed to reduce agitation,
 * anxiety, and restlessness in patients living with dementia and Alzheimer's.
 * Generates continuous soft harmonic chords (432 Hz Pythagorean peaceful tuning).
 */
class CalmingAudioSynthesizer {

  private var audioTrack: AudioTrack? = null
  private var playbackJob: Job? = null
  private val scope = CoroutineScope(Dispatchers.Default)

  private val _isPlaying = MutableStateFlow(false)
  val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

  private val _volume = MutableStateFlow(0.6f)
  val volume: StateFlow<Float> = _volume.asStateFlow()

  private val sampleRate = 44100
  // Frequencies for peaceful C major 9th harmonic chord: C4 (256/261Hz), E4 (324/327Hz), G4 (384/392Hz), B4 (486/493Hz)
  private val baseFrequencies = doubleArrayOf(216.0, 324.0, 432.0, 540.0)

  fun togglePlayback() {
    if (_isPlaying.value) {
      pause()
    } else {
      play()
    }
  }

  fun play() {
    if (_isPlaying.value) return
    _isPlaying.value = true

    playbackJob = scope.launch {
      val minBufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
      )
      val bufferSize = minBufferSize.coerceAtLeast(sampleRate / 4)

      val track = AudioTrack.Builder()
        .setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        )
        .setAudioFormat(
          AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()
        )
        .setBufferSizeInBytes(bufferSize * 2)
        .setTransferMode(AudioTrack.MODE_STREAM)
        .build()

      audioTrack = track
      track.play()

      val buffer = ShortArray(bufferSize)
      var phase0 = 0.0
      var phase1 = 0.0
      var phase2 = 0.0
      var phase3 = 0.0
      var lfoPhase = 0.0

      try {
        while (isActive && _isPlaying.value) {
          val vol = _volume.value
          for (i in buffer.indices) {
            // Gentle Low-Frequency Oscillator (LFO) for breathing effect (~0.1 Hz breathing rhythm)
            val lfo = 0.7 + 0.3 * sin(lfoPhase)
            lfoPhase += 2.0 * Math.PI * 0.1 / sampleRate
            if (lfoPhase >= 2.0 * Math.PI) lfoPhase -= 2.0 * Math.PI

            // Soft sinusoidal harmonic tones
            val s0 = sin(phase0) * 0.35
            val s1 = sin(phase1) * 0.25
            val s2 = sin(phase2) * 0.25
            val s3 = sin(phase3) * 0.15

            phase0 += 2.0 * Math.PI * baseFrequencies[0] / sampleRate
            phase1 += 2.0 * Math.PI * baseFrequencies[1] / sampleRate
            phase2 += 2.0 * Math.PI * baseFrequencies[2] / sampleRate
            phase3 += 2.0 * Math.PI * baseFrequencies[3] / sampleRate

            if (phase0 >= 2.0 * Math.PI) phase0 -= 2.0 * Math.PI
            if (phase1 >= 2.0 * Math.PI) phase1 -= 2.0 * Math.PI
            if (phase2 >= 2.0 * Math.PI) phase2 -= 2.0 * Math.PI
            if (phase3 >= 2.0 * Math.PI) phase3 -= 2.0 * Math.PI

            val mixed = (s0 + s1 + s2 + s3) * lfo * vol
            buffer[i] = (mixed * 32767.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
          }
          track.write(buffer, 0, buffer.size)
        }
      } catch (_: Exception) {
      } finally {
        try {
          track.stop()
          track.release()
        } catch (_: Exception) {
        }
      }
    }
  }

  fun pause() {
    _isPlaying.value = false
    playbackJob?.cancel()
    playbackJob = null
    try {
      audioTrack?.stop()
      audioTrack?.release()
    } catch (_: Exception) {
    }
    audioTrack = null
  }

  fun setVolume(newVolume: Float) {
    _volume.value = newVolume.coerceIn(0.1f, 1.0f)
  }

  fun release() {
    pause()
  }
}
