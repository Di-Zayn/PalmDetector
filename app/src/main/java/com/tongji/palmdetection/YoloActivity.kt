package com.tongji.palmdetection

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
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
        // 适用于30 api 
        // 当前手机的sdk是29 所以不能调整
//        val ic = window.insetsController
//        if (ic != null) {
//            ic.hide(WindowInsets.Type.statusBars());
//            ic.hide(WindowInsets.Type.navigationBars());
//        }
        window.statusBarColor = Color.TRANSPARENT

        // 模型初始化
        val ret_init = yolov5ncnn.init(assets)
        if (ret_init) Toast.makeText(this, "yolov5n初始化成功", Toast.LENGTH_LONG)
            .show() else Toast.makeText(this, "yolov5n初始化失败", Toast.LENGTH_LONG).show()

        // 开始检测
        val cameraPreview = findViewById<PreviewView>(R.id.camera_preview)
        val canvas = findViewById<ImageView>(R.id.box_label_canvas)
        val costTimeText = findViewById<TextView>(R.id.cost_time)

        val imageAnalyzer = ImageAnalyzer(
            this@YoloActivity,
            cameraPreview,
            costTimeText,
            canvas,
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

        cameraProcess.startCamera(this@YoloActivity, imageAnalyzer, cameraPreview)
    }
}

