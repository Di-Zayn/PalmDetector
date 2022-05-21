package com.tongji.palmdetection

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView

class YoloActivity : AppCompatActivity() {
    private val yolov5ncnn = YoloV5Ncnn()
    private val cameraProcess = CameraProcess()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 去掉标题栏
        setContentView(R.layout.camera_layout)

        // 打开app的时候隐藏顶部状态栏
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        // 获取手机摄像头拍照旋转参数
        val rotation = windowManager.defaultDisplay.rotation

        // 模型初始化
        val ret_init = yolov5ncnn.init(assets)
        if (ret_init) Toast.makeText(this, "yolov5n初始化成功", Toast.LENGTH_LONG)
            .show() else Toast.makeText(this, "yolov5n初始化失败", Toast.LENGTH_LONG).show()

        // 开始检测
        val cameraPreview = findViewById<PreviewView>(R.id.camera_preview)
        val canvas = findViewById<ImageView>(R.id.box_label_canvas)
        val imageAnalyse = ImageAnalyse(
            this@YoloActivity,
            cameraPreview,
            canvas,
            rotation,
            yolov5ncnn
        )

        val button = findViewById<ComposeView>(R.id.button)
        button.setContent {
            Button(onClick = {
                println("Click")
            }){
                Text(text = "Button")
            }
        }

        cameraProcess.startCamera(this@YoloActivity, imageAnalyse, cameraPreview)
    }
}

