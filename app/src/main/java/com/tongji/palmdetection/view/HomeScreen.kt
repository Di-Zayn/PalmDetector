package com.tongji.palmdetection.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.tongji.palmdetection.YoloContract


@Composable
fun HomeScreen(
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = YoloContract()) {
        it?.let {
            android.widget.Toast.makeText(context, "$it", android.widget.Toast.LENGTH_LONG)
        }
    }
    Button(onClick = {
        launcher.launch(null)
    }) {
        Text(text = "Click")
    }
}