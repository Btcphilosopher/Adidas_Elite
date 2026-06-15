package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.data.local.AdidasEliteRepository
import com.example.data.local.AppDatabase
import com.example.ui.AdidasEliteViewModel
import com.example.ui.AdidasEliteViewModelFactory
import com.example.ui.screens.MainAppContainer
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Instantiate local Room database & repo
    val database = AppDatabase.getDatabase(applicationContext)
    val repository = AdidasEliteRepository(database)

    // Instantiate view model with local repo factory
    val viewModel: AdidasEliteViewModel by viewModels {
      AdidasEliteViewModelFactory(repository)
    }

    enableEdgeToEdge()
    setContent {
      MyApplicationTheme(dynamicColor = false) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          MainAppContainer(
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize()
          )
        }
      }
    }
  }
}
