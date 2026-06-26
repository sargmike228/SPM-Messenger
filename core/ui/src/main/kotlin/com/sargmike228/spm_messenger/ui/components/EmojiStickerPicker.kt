package com.sargmike228.spm_messenger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Emoji(
    val unicode: String,
    val name: String
)

val POPULAR_EMOJIS = listOf(
    Emoji("😀", "smile"),
    Emoji("😂", "joy"),
    Emoji("❤️", "love"),
    Emoji("🔥", "fire"),
    Emoji("👍", "thumbsup"),
    Emoji("👏", "clap"),
    Emoji("😍", "heart_eyes"),
    Emoji("🎉", "party"),
    Emoji("😭", "cry"),
    Emoji("😱", "scared"),
    Emoji("🤔", "thinking"),
    Emoji("😎", "cool"),
)

val STICKERS = listOf(
    "🎨", "🎭", "🎪", "🎬", "🎤", "🎧",
    "🎸", "🎹", "🎺", "🎻", "🥁", "🎲"
)

@Composable
fun EmojiPicker(
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp)
    ) {
        Text("Выберите эмодзи:", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        var rows = (POPULAR_EMOJIS.size + 5) / 6
        repeat(rows) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(6) { colIndex ->
                    val emojiIndex = rowIndex * 6 + colIndex
                    if (emojiIndex < POPULAR_EMOJIS.size) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onEmojiSelected(POPULAR_EMOJIS[emojiIndex].unicode) }
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(POPULAR_EMOJIS[emojiIndex].unicode, fontSize = 24.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StickerPicker(
    onStickerSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp)
    ) {
        Text("Выберите стикер:", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        var rows = (STICKERS.size + 5) / 6
        repeat(rows) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(6) { colIndex ->
                    val stickerIndex = rowIndex * 6 + colIndex
                    if (stickerIndex < STICKERS.size) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onStickerSelected(STICKERS[stickerIndex]) }
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(STICKERS[stickerIndex], fontSize = 32.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.size(50.dp))
                    }
                }
            }
        }
    }
}