package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ChatMessage

@Composable
fun CompanionScreen(
  chatMessages: List<ChatMessage>,
  language: AppLanguage,
  isLargeText: Boolean,
  onSendMessage: (String) -> Unit,
  onSpeakText: (String) -> Unit,
  onClearChat: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isArabic = language == AppLanguage.ARABIC
  var messageInput by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  val bodySize = if (isLargeText) 18.sp else 15.sp

  val promptChips = if (isArabic) {
    listOf(
      "أشعر بالوحدة اليوم",
      "أشعر ببعض التعب والإرهاق",
      "نسيت موعد الدواء",
      "أشعر بتحسن والحمد لله",
      "أريد نصيحة للاسترخاء وراحة البال",
      "حدثني عن فريق مشروع التخرج"
    )
  } else {
    listOf(
      "I feel a bit lonely today",
      "I feel tired and need rest",
      "I'm worried about my pills",
      "I feel great and healthy today!",
      "Can you give me a relaxation tip?",
      "Tell me about the graduation project"
    )
  }

  LaunchedEffect(chatMessages.size) {
    if (chatMessages.isNotEmpty()) {
      listState.animateScrollToItem(chatMessages.size - 1)
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    // Header Bar
    Card(
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
      ),
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(42.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(24.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = if (isArabic) "رفيق الرعاية والتعاطف الذكي" else "AI Empathy Care Companion",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
              text = if (isArabic) "محادثة مريحة للاستماع والدعم النفسي والصحي" else "Comforting conversational support for seniors",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )
          }
        }

        IconButton(onClick = onClearChat) {
          Icon(
            imageVector = Icons.Default.DeleteSweep,
            contentDescription = "Clear Chat",
            tint = MaterialTheme.colorScheme.secondary
          )
        }
      }
    }

    // Chat messages list
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      contentPadding = PaddingValues(vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(chatMessages, key = { it.id }) { msg ->
        val isUser = msg.isUser
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
          verticalAlignment = Alignment.Bottom
        ) {
          if (!isUser) {
            Surface(
              shape = CircleShape,
              color = MaterialTheme.colorScheme.primaryContainer,
              modifier = Modifier
                .size(34.dp)
                .padding(bottom = 4.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  Icons.Default.AutoAwesome,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp),
                  tint = MaterialTheme.colorScheme.primary
                )
              }
            }
            Spacer(modifier = Modifier.width(8.dp))
          }

          Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 290.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isUser) 18.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 18.dp
              ),
              color = if (isUser) MaterialTheme.colorScheme.primary
              else MaterialTheme.colorScheme.surfaceVariant,
              tonalElevation = 2.dp
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = msg.text,
                  style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = bodySize,
                    lineHeight = 22.sp
                  ),
                  color = if (isUser) MaterialTheme.colorScheme.onPrimary
                  else MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (!isUser) {
                  Spacer(modifier = Modifier.height(6.dp))
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                  ) {
                    IconButton(
                      onClick = { onSpeakText(msg.text) },
                      modifier = Modifier.size(30.dp)
                    ) {
                      Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Speak Aloud",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                      )
                    }
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = msg.timestamp,
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
              modifier = Modifier.padding(horizontal = 4.dp)
            )
          }

          if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              shape = CircleShape,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier
                .size(34.dp)
                .padding(bottom = 4.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  Icons.Default.Person,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp),
                  tint = MaterialTheme.colorScheme.onPrimary
                )
              }
            }
          }
        }
      }
    }

    // Quick suggestion chips
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(promptChips) { prompt ->
        AssistChip(
          onClick = {
            onSendMessage(prompt)
          },
          label = { Text(prompt, fontSize = 12.sp) },
          leadingIcon = {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
          }
        )
      }
    }

    // Input Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 90.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = messageInput,
        onValueChange = { messageInput = it },
        placeholder = {
          Text(if (isArabic) "اكتب رسالتك لرفيق الرعاية..." else "Type message to companion...")
        },
        modifier = Modifier
          .weight(1f)
          .testTag("companion_chat_input"),
        shape = RoundedCornerShape(24.dp),
        maxLines = 3
      )

      Spacer(modifier = Modifier.width(8.dp))

      IconButton(
        onClick = {
          if (messageInput.isNotBlank()) {
            val textToSend = messageInput
            messageInput = ""
            onSendMessage(textToSend)
          }
        },
        modifier = Modifier
          .size(52.dp)
          .clip(CircleShape)
          .background(
            if (messageInput.isNotBlank()) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant
          )
          .testTag("companion_send_button"),
        enabled = messageInput.isNotBlank()
      ) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = "Send",
          tint = if (messageInput.isNotBlank()) MaterialTheme.colorScheme.onPrimary
          else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
