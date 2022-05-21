package com.tongji.palmdetection.component

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.tongji.palmdetection.ImageAnalyzer
import com.tongji.palmdetection.R
import com.tongji.palmdetection.YoloV5Ncnn



// 可尝试直接调用这个组件 但布局存在问题

//        setContent {
//            Camera(rotation, yolov5ncnn)
//        }
@Composable
fun Camera(rotation: Int, yolo: YoloV5Ncnn) {
    // context 的问题 还是 analyse 的问题

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    Toast.makeText(context, "$rotation", Toast.LENGTH_LONG)

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    val previewView = remember {
        PreviewView(context).apply {
            id = R.id.camera_preview
//            id = R.id.preview_view
        }
    }

    val imageView = remember {
        ImageView(context).apply {
            id = R.id.box_label_canvas
//            id = R.id.canvas
        }
    }

    val textView = remember {
        TextView(context).apply {
            id = R.id.cost_time
        }
    }

    val imgAnalyse = remember {
        ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

    val imageAnalyzer = ImageAnalyzer(
        context,
        previewView,
        textView,
        imageView,
        yolo
    )

    imgAnalyse.setAnalyzer(
        ContextCompat.getMainExecutor(context), imageAnalyzer)

    AndroidView(factory = {previewView}, modifier = Modifier.fillMaxHeight()) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            preview.setSurfaceProvider(previewView.surfaceProvider)
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imgAnalyse,
                )
            } catch (e: Exception) {
                Log.e("Exception", e.toString())
            }

        }, ContextCompat.getMainExecutor(context))
    }
}