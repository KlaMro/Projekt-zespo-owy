package com.example.quizloop.feature.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.material3.AssistChip
import com.example.quizloop.core.model.QuizSummary
import com.example.quizloop.core.model.QuizQuestion
import kotlinx.coroutines.launch

@Composable
fun CreateQuizScreen(
    onSaveDraft: suspend (String, String, List<QuizQuestion>) -> QuizSummary,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var createdQuiz by remember { mutableStateOf<QuizSummary?>(null) }

    // Editor state
    var questionPrompt by remember { mutableStateOf("") }
    val optionTexts = remember { mutableStateListOf("", "", "") }
    var correctIndex by remember { mutableIntStateOf(0) }
    val questions = remember { mutableStateListOf<QuizQuestion>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Create quiz", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Quiz title") },
            singleLine = true
        )
        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Subject") },
            singleLine = true
        )

        // Question editor
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Add question", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = questionPrompt,
                    onValueChange = { questionPrompt = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Question prompt") }
                )
                // Options
                optionTexts.forEachIndexed { idx, opt ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = opt,
                            onValueChange = { value -> optionTexts[idx] = value },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Option ${idx + 1}") }
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                            AssistChip(onClick = { correctIndex = idx }, label = { Text(if (correctIndex == idx) "Correct" else "Mark" ) })
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { optionTexts.add("") }) { Text("Add option") }
                    Button(onClick = {
                        // add question
                        val opts = optionTexts.toList().filter { it.isNotBlank() }
                        if (questionPrompt.isNotBlank() && opts.size >= 2) {
                            questions.add(QuizQuestion(prompt = questionPrompt, options = opts, correctIndex = correctIndex.coerceIn(0, opts.lastIndex)))
                            // reset
                            questionPrompt = ""
                            optionTexts.clear(); optionTexts.addAll(listOf("", "", ""))
                            correctIndex = 0
                        }
                    }) { Text("Add question") }
                }
            }
        }

        // List of added questions
        if (questions.isNotEmpty()) {
            Text("Questions (${questions.size})", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
            questions.forEachIndexed { i, q ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${i + 1}. ${q.prompt}", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                        q.options.forEachIndexed { oi, o ->
                            Text((if (q.correctIndex == oi) "* " else "  ") + o)
                        }
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                scope.launch {
                    createdQuiz = onSaveDraft(title, subject, questions.toList())
                }
            }) {
                Text("Save quiz")
            }
            Button(onClick = onBack) { Text("Back") }
        }

        if (createdQuiz != null) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Saved: ${createdQuiz!!.title}")
                    Text("Room code: ${createdQuiz!!.roomCode}")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
