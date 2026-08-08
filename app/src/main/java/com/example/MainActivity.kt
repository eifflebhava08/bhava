package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.InstructionModal
import com.example.ui.screens.ScoreScreen
import com.example.ui.screens.StartScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameScreen
import com.example.viewmodel.MakeupGameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MakeupStudioApp()
            }
        }
    }
}

@Composable
fun MakeupStudioApp(viewModel: MakeupGameViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.currentScreen) {
                GameScreen.START_SCREEN -> {
                    StartScreen(
                        state = state,
                        onStartGame = { viewModel.startGame() },
                        onSelectCharacter = { index -> viewModel.selectCharacter(index) },
                        onSelectChallenge = { index -> viewModel.selectChallenge(index) },
                        onShowInstructions = { viewModel.toggleInstructionDialog(true) },
                        onNavigateGallery = { viewModel.navigateTo(GameScreen.GALLERY_SCREEN) }
                    )
                }

                GameScreen.STUDIO_SCREEN -> {
                    StudioScreen(
                        state = state,
                        onSelectCategory = { category -> viewModel.selectCategory(category) },
                        onSelectOption = { category, option -> viewModel.selectOption(category, option) },
                        onUndo = { viewModel.undo() },
                        onReset = { viewModel.reset() },
                        onRandomLook = { viewModel.randomLook() },
                        onShowScore = { viewModel.evaluateAndShowScore() },
                        onShowInstructions = { viewModel.toggleInstructionDialog(true) }
                    )
                }

                GameScreen.SCORE_SCREEN -> {
                    ScoreScreen(
                        state = state,
                        onNextCharacter = { viewModel.nextCharacter() },
                        onRestyle = { viewModel.navigateTo(GameScreen.STUDIO_SCREEN) },
                        onNavigateGallery = { viewModel.navigateTo(GameScreen.GALLERY_SCREEN) }
                    )
                }

                GameScreen.GALLERY_SCREEN -> {
                    GalleryScreen(
                        state = state,
                        onBack = { viewModel.navigateTo(GameScreen.START_SCREEN) }
                    )
                }
            }

            // Instruction Modal Dialog
            if (state.showInstructionDialog) {
                InstructionModal(onDismiss = { viewModel.toggleInstructionDialog(false) })
            }
        }
    }
}

