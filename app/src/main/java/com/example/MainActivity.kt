package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.CareApp
import com.example.ui.theme.ReturnTheFavorTheme
import com.example.viewmodel.CareViewModel

class MainActivity : ComponentActivity() {

  private val careViewModel: CareViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ReturnTheFavorTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          CareApp(viewModel = careViewModel)
        }
      }
    }
  }
}
