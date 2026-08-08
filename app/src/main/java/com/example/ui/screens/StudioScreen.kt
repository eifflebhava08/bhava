package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MakeupCatalog
import com.example.model.MakeupCategory
import com.example.model.MakeupOption
import com.example.ui.components.CharacterAvatarCanvas
import com.example.ui.theme.SalonDarkText
import com.example.ui.theme.SalonGold
import com.example.ui.theme.SalonPinkContainer
import com.example.ui.theme.SalonPinkPrimary
import com.example.ui.theme.SalonPurpleContainer
import com.example.ui.theme.SalonPurplePrimary
import com.example.viewmodel.MakeupGameState

@Composable
fun StudioScreen(
    state: MakeupGameState,
    onSelectCategory: (MakeupCategory) -> Unit,
    onSelectOption: (MakeupCategory, MakeupOption) -> Unit,
    onUndo: () -> Unit,
    onReset: () -> Unit,
    onRandomLook: () -> Unit,
    onShowScore: () -> Unit,
    onShowInstructions: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0F5))
    ) {
        // 1. Top Header & Challenge Bar
        StudioHeader(
            state = state,
            onUndo = onUndo,
            onReset = onReset,
            onRandomLook = onRandomLook,
            onShowScore = onShowScore,
            onShowInstructions = onShowInstructions
        )

        // 2. Center Interactive Character Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                CharacterAvatarCanvas(
                    character = state.activeCharacter,
                    currentLook = state.currentLook,
                    isSparkling = state.isSparkling,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                )
            }
        }

        // 3. Category Tab Bar & Options Selector
        StudioControlsPanel(
            state = state,
            onSelectCategory = onSelectCategory,
            onSelectOption = onSelectOption
        )
    }
}

@Composable
private fun StudioHeader(
    state: MakeupGameState,
    onUndo: () -> Unit,
    onReset: () -> Unit,
    onRandomLook: () -> Unit,
    onShowScore: () -> Unit,
    onShowInstructions: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Character & Challenge info row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Styling: ${state.activeCharacter.name} 🌸",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SalonPinkPrimary
                        )
                    )
                    Text(
                        text = state.activeChallenge.title,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = SalonPurplePrimary
                        )
                    )
                }

                // Done / Show Look Main Action Button
                Button(
                    onClick = onShowScore,
                    colors = ButtonDefaults.buttonColors(containerColor = SalonPinkPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Done", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Done / Score ✨", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Actions Toolbar Row (Undo, Reset, Random, Help)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Undo Button
                IconButton(
                    onClick = onUndo,
                    enabled = state.historyStack.isNotEmpty(),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (state.historyStack.isNotEmpty()) SalonPinkContainer else Color(0xFFF5F5F5))
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Undo",
                        tint = if (state.historyStack.isNotEmpty()) SalonPinkPrimary else Color.Gray
                    )
                }

                // Reset Button
                IconButton(
                    onClick = onReset,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(SalonPurpleContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Reset",
                        tint = SalonPurplePrimary
                    )
                }

                // Random Look Button
                IconButton(
                    onClick = onRandomLook,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(SalonPinkContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Random Look",
                        tint = SalonPinkPrimary
                    )
                }

                // Instructions Button
                IconButton(
                    onClick = onShowInstructions,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(SalonPurpleContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Help",
                        tint = SalonPurplePrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun StudioControlsPanel(
    state: MakeupGameState,
    onSelectCategory: (MakeupCategory) -> Unit,
    onSelectOption: (MakeupCategory, MakeupOption) -> Unit
) {
    Card(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            // Category Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                items(MakeupCategory.values()) { category ->
                    val isSelected = category == state.activeCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) SalonPinkPrimary else SalonPinkContainer)
                            .clickable { onSelectCategory(category) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = category.iconRes, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = category.displayName,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else SalonDarkText
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Options Row for selected active category
            val options = MakeupCatalog.optionsByCategory[state.activeCategory] ?: emptyList()
            val currentSelectedOption = state.currentLook[state.activeCategory]

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                items(options) { option ->
                    val isSelected = currentSelectedOption?.id == option.id

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) SalonPurpleContainer else Color(0xFFFAFAFA)
                        ),
                        modifier = Modifier
                            .width(90.dp)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) SalonPurplePrimary else Color(0xFFEEEEEE),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelectOption(state.activeCategory, option) }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            // Swatch Circle
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (option.hexColor == "#00000000") Color.LightGray.copy(alpha = 0.3f)
                                        else option.parsedColor
                                    )
                                    .border(1.dp, Color.White, CircleShape)
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = option.name,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 10.sp,
                                    color = SalonDarkText,
                                    textAlign = TextAlign.Center
                                ),
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}
