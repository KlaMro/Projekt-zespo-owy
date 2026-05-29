package com.example.quizloop

import androidx.lifecycle.ViewModel
import com.example.quizloop.core.data.QuizRepository
import com.example.quizloop.core.model.LiveQuizSession
import com.example.quizloop.core.model.QuizSummary
import kotlinx.coroutines.flow.StateFlow

class QuizLoopViewModel(
    private val repository: QuizRepository
) : ViewModel() {
    val quizzes: StateFlow<List<QuizSummary>> = repository.quizzes
    val liveSession: StateFlow<LiveQuizSession?> = repository.liveSession

    suspend fun createQuiz(title: String, subject: String, questionCount: Int): QuizSummary {
        return repository.createQuiz(title = title, subject = subject, questionCount = questionCount)
    }

    suspend fun joinRoom(roomCode: String): LiveQuizSession {
        return repository.joinRoom(roomCode)
    }

    suspend fun startSinglePlayer(quizId: String): LiveQuizSession? {
        return repository.startSinglePlayer(quizId)
    }

    suspend fun saveQuiz(title: String, subject: String, questions: List<com.example.quizloop.core.model.QuizQuestion>): QuizSummary {
        return repository.saveQuiz(title, subject, questions)
    }

    fun submitAnswer(optionIndex: Int) {
        repository.submitAnswer(optionIndex)
    }

    fun advanceQuestion() {
        repository.advanceQuestion()
    }

    fun endSession() {
        repository.endSession()
    }
}
