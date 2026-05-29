package com.example.quizloop.feature.play

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.quizloop.core.model.LiveQuizSession

@Composable
fun PlayQuizScreen(
    session: LiveQuizSession?,
    onAnswer: (Int) -> Unit,
    onNext: () -> Unit,
    onExit: () -> Unit
) {
    if (session == null) {
        EmptySessionState(onExit = onExit)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 4.dp,
            shadowElevation = 10.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(session.quizTitle, style = MaterialTheme.typography.headlineMedium, color = androidx.compose.ui.graphics.Color.White)
                Text("Room ${session.roomCode} · ${session.subject}", color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f))
                Text("Players online: ${session.playersOnline}", color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f))
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Question ${session.progressLabel}", style = MaterialTheme.typography.titleLarge)
                Text(session.currentQuestion?.prompt ?: "No active question")
                session.currentQuestion?.options?.forEachIndexed { index, option ->
                    val isSelected = session.selectedAnswerIndex == index
                    val buttonColors = if (isSelected) {
                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    }
                    OutlinedButton(
                        onClick = { if (session.selectedAnswerIndex == null) onAnswer(index) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = buttonColors
                    ) {
                        Text(option)
                    }
                }
                Text(session.feedbackMessage, style = MaterialTheme.typography.bodyMedium)
                if (session.selectedAnswerIndex != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ElevatedButton(onClick = onNext) {
                            Text(if (session.currentQuestionIndex >= session.questions.lastIndex) "Finish round" else "Next question")
                        }
                        OutlinedButton(onClick = onExit) {
                            Text("Exit room")
                        }
                    }
                } else {
                    OutlinedButton(onClick = onExit) {
                        Text("Leave room")
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Why this structure works", style = MaterialTheme.typography.titleLarge)
                Text("The UI reads from a session object, while the repository owns the mutation logic. That makes it easier to replace the demo backend with Firebase later.")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun EmptySessionState(onExit: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No live room is active right now.", style = MaterialTheme.typography.headlineMedium)
        Text("Join a room first to open the playable quiz experience.")
        Button(onClick = onExit) {
            Text("Back to home")
        }
    }
}
