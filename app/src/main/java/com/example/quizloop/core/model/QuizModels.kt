package com.example.quizloop.core.model

data class QuizSummary(
    val id: String,
    val title: String,
    val subject: String,
    val roomCode: String,
    val questionCount: Int
)

data class QuizQuestion(
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int
)

data class LiveQuizSession(
    val roomCode: String,
    val quizTitle: String,
    val subject: String,
    val playersOnline: Int,
    val questions: List<QuizQuestion>,
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val score: Int = 0,
    val feedbackMessage: String = "Waiting for the next player to join."
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentQuestionIndex)

    val progressLabel: String
        get() = "${currentQuestionIndex + 1}/${questions.size}"

    val isFinished: Boolean
        get() = currentQuestionIndex >= questions.lastIndex && selectedAnswerIndex != null
}
