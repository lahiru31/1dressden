package com.khadar3344.myshop.multimedia

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import java.io.File
import java.io.IOException
import javax.inject.Inject

class MediaManager @Inject constructor(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording: Boolean = false
    private var isPlaying: Boolean = false

    fun startRecording(outputFile: File) {
        try {
            mediaRecorder = MediaRecorder(context).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile.absolutePath)
                
                try {
                    prepare()
                    start()
                    this@MediaManager.isRecording = true
                    Log.d("MediaManager", "Started recording to ${outputFile.absolutePath}")
                } catch (e: IOException) {
                    Log.e("MediaManager", "Failed to start recording", e)
                    release()
                }
            }
        } catch (e: Exception) {
            Log.e("MediaManager", "Error initializing recorder", e)
        }
    }

    fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false
            Log.d("MediaManager", "Stopped recording")
        } catch (e: Exception) {
            Log.e("MediaManager", "Error stopping recording", e)
        }
    }

    fun startPlaying(audioUri: Uri) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, audioUri)
                prepare()
                start()
                this@MediaManager.isPlaying = true
                
                setOnCompletionListener {
                    this@MediaManager.isPlaying = false
                    release()
                    mediaPlayer = null
                }
            }
            Log.d("MediaManager", "Started playing audio")
        } catch (e: Exception) {
            Log.e("MediaManager", "Error playing audio", e)
        }
    }

    fun stopPlaying() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }
            mediaPlayer = null
            isPlaying = false
            Log.d("MediaManager", "Stopped playing audio")
        } catch (e: Exception) {
            Log.e("MediaManager", "Error stopping playback", e)
        }
    }

    fun isRecording(): Boolean = isRecording
    fun isPlaying(): Boolean = isPlaying

    fun release() {
        stopRecording()
        stopPlaying()
    }
}
