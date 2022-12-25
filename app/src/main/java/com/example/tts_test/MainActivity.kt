package com.example.tts_test

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.tts_test.core.extensions.log
import com.example.tts_test.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import org.opencv.android.*
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    //private lateinit var textToSpeech: TextToSpeech

    private lateinit var cameraBridgeViewBase: CameraBridgeViewBase
    private val baseLoaderCallback = BaseCallback()
    private var mRgba: Mat? = null
    private var mGray: Mat? = null
    private var bitmap: Bitmap? = null

    private lateinit var recognizer: TextRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if(OpenCVLoader.initDebug()) {
            log("tts", "OpenCV is online!")
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
        else {
            log("tts", "OpenCV is gone(")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, baseLoaderCallback)
        }
    }

    private fun initViews() {

        cameraBridgeViewBase = binding.javaCamera
        cameraBridgeViewBase.visibility = SurfaceView.VISIBLE
        cameraBridgeViewBase.setCvCameraViewListener(this)
        recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())

        binding.shootButton.setOnClickListener {
            val aRgba = mRgba?.t()
            if(aRgba != null) {
                Core.flip(aRgba, mRgba, 1)
                aRgba.release()

                val cols = mRgba?.cols()
                val rows = mRgba?.rows()
                if (cols != null && rows != null) {
                    bitmap = Bitmap.createBitmap(cols, rows, Bitmap.Config.ARGB_8888)
                    Utils.matToBitmap(mRgba, bitmap)
                    if (bitmap != null) {
                        val inputImage = InputImage.fromBitmap(bitmap!!, 0)
                        val result = recognizer.process(inputImage)
                            .addOnSuccessListener { text ->
                                Toast.makeText(
                                    this,
                                    "${text.text} ${text.textBlocks.size}",
                                    Toast.LENGTH_SHORT).show()
                                mainViewModel.summarizeText(text = text.text)
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "Exception: $exception", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
            }
        }
    }

    private fun initObservers() {
        mainViewModel.summarizedTextLiveData.observe(this) { resultText ->
            if (resultText.isNotBlank()) {
                MainAlertDialog().showTextDialog(
                    this,
                    "Summarized Text",
                    resultText,
                    true,
                    R.style.Hackathon_Alert
                )
            }
        }
    }


    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
        mGray = Mat(height, width, CvType.CV_8UC1)
    }

    override fun onCameraViewStopped() {
        mRgba?.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        mRgba = inputFrame?.rgba()
        mGray = inputFrame?.gray()

        return mRgba ?: Mat()
    }

    inner class BaseCallback: BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when(status) {
                LoaderCallbackInterface.SUCCESS -> {
                    log("tts","LoaderCallback active!")
                    cameraBridgeViewBase.enableView()
                }
                else ->  super.onManagerConnected(status)
            }
        }
    }

}