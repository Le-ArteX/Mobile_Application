package com.example.photogalleryapp

import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FullscreenActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var matrix: Matrix
    private var scale = 1f
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        imageView = findViewById(R.id.ivFullscreen)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        val resId = intent.getIntExtra("resId", 0)
        if (resId != 0) {
            imageView.setImageResource(resId)
        }

        btnBack.setOnClickListener {
            finish()
        }

        matrix = Matrix()
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { scaleGestureDetector.onTouchEvent(it) }
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale *= detector.scaleFactor
            scale = Math.max(0.1f, Math.min(scale, 5.0f))
            matrix.setScale(scale, scale, detector.focusX, detector.focusY)
            imageView.imageMatrix = matrix
            return true
        }
    }
}
