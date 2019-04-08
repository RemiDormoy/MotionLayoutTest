package com.rdo.octo.motionlayouttest

import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import kotlinx.android.synthetic.main.ticket_activity.*
import com.google.android.gms.vision.CameraSource
import java.io.IOException


class TicketActivity : AppCompatActivity() {

    private var cameraSource: CameraSource? = null

    private var mCamera: Camera? = null
    private var preview: CameraPreview? = null

    val metadata by lazy {
        FirebaseVisionImageMetadata.Builder()
            .setWidth(480) // 480x360 is typically sufficient for
            .setHeight(360) // image recognition
            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticket_activity)
        supportActionBar?.hide()
        safeCameraOpen()
        mCamera?.setPreviewCallbackWithBuffer { data, _ ->
            val image = FirebaseVisionImage.fromByteArray(data,metadata)
            val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
            detector.processImage(image).addOnSuccessListener { firebaseVisionText ->
                recognizedText.text = firebaseVisionText.text
            }
                .addOnFailureListener {
                    Toast.makeText(this, "error : $it", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun safeCameraOpen(): Boolean {
        return try {
            releaseCameraAndPreview()
            mCamera = Camera.open()
            mCamera?.startPreview()
            mCamera?.setDisplayOrientation(90)
            val params = mCamera?.parameters
            params?.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera?.setParameters(params)
            preview = CameraPreview(this, mCamera!!)
            true
        } catch (e: Exception) {
            Log.e(getString(R.string.app_name), "failed to open Camera")
            e.printStackTrace()
            false
        }
    }


    private fun releaseCameraAndPreview() {
        mCamera?.also { camera ->
            camera.release()
            mCamera = null
        }
    }

}
