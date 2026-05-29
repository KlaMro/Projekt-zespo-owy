package com.example.quizloop.feature.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
// Assist chips removed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quizloop.core.model.LiveQuizSession
import com.example.quizloop.core.model.QuizSummary

@Composable
fun HomeScreen(
    quizzes: List<QuizSummary>,
    liveSession: LiveQuizSession?,
    onCreateQuiz: () -> Unit,
    onCreateLobby: () -> Unit,
    onJoinQuiz: () -> Unit,
    onContinueSession: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 4.dp,
            shadowElevation = 10.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
                    .padding(22.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "QuizLoop",
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "A small Kahoot-style app for creating quick quizzes and running live play sessions.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.92f)
                    )
                    // removed decorative small chips (Create / Join / Play)
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            androidx.compose.material3.ElevatedButton(
                onClick = onCreateQuiz,
                modifier = Modifier.weight(1f),
                colors = androidx.compose.material3.ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Create quiz", color = MaterialTheme.colorScheme.onPrimary)
            }
            androidx.compose.material3.OutlinedButton(
                onClick = onCreateLobby,
                modifier = Modifier.weight(1f)
            ) {
                Text("Create lobby")
            }
            androidx.compose.material3.OutlinedButton(
                onClick = onJoinQuiz,
                modifier = Modifier.weight(1f)
            ) {
                Text("Join room")
            }
        }

        if (liveSession != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Live room ready", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "${liveSession.quizTitle} is waiting in room ${liveSession.roomCode}.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(onClick = onContinueSession) {
                        Text("Continue live session")
                    }
                }
            }
        }

        Text("Your quiz drafts", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "These local drafts are stored in-memory for now, so the project stays small and easy to extend later.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (quizzes.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("No drafts yet", style = MaterialTheme.typography.titleLarge)
                    Text("Create a quiz to see it appear here.")
                }
            }
        } else {
            quizzes.forEach { quiz ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(quiz.title, style = MaterialTheme.typography.titleLarge)
                        Text("${quiz.subject} · ${quiz.questionCount} questions")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Room code: ${quiz.roomCode}", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
