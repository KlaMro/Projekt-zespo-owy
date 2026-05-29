package com.example.quizloop

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quizloop.feature.create.CreateQuizScreen
import com.example.quizloop.feature.home.HomeScreen
import com.example.quizloop.feature.join.JoinQuizScreen
import com.example.quizloop.feature.play.PlayQuizScreen
import com.example.quizloop.feature.lobby.CreateLobbyScreen
import com.example.quizloop.feature.lobby.SinglePlayerSelectScreen
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.quizloop.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizLoopApp(viewModel: QuizLoopViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: Routes.Home
    val liveSession by viewModel.liveSession.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            Routes.Create -> "Create a Quiz"
                            Routes.Join -> "Join a Room"
                            Routes.Play -> "Live Quiz"
                            else -> "QuizLoop"
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home,
            modifier = androidx.compose.ui.Modifier
                .padding(innerPadding)
        ) {
            composable(Routes.Home) {
                val quizzes by viewModel.quizzes.collectAsState()
                HomeScreen(
                    quizzes = quizzes,
                    liveSession = liveSession,
                    onCreateQuiz = { navController.navigate(Routes.Create) },
                    onCreateLobby = { navController.navigate(Routes.CreateLobby) },
                    onJoinQuiz = { navController.navigate(Routes.Join) },
                    onContinueSession = { navController.navigate(Routes.Play) }
                )
            }
            composable(Routes.Create) {
                CreateQuizScreen(
                    onSaveDraft = { title, subject, questions -> viewModel.saveQuiz(title, subject, questions) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.CreateLobby) {
                CreateLobbyScreen(
                    onSinglePlayer = { navController.navigate(Routes.SingleSelect) },
                    onMultiPlayer = { /* inactive for now */ },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.SingleSelect) {
                val quizzes by viewModel.quizzes.collectAsState()
                val coroutineScope = rememberCoroutineScope()
                SinglePlayerSelectScreen(
                    quizzes = quizzes,
                    onStart = { quizId ->
                        coroutineScope.launch {
                            viewModel.startSinglePlayer(quizId)
                            navController.navigate(Routes.Play)
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.Join) {
                JoinQuizScreen(
                    onJoinRoom = viewModel::joinRoom,
                    onJoined = { navController.navigate(Routes.Play) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.Play) {
                PlayQuizScreen(
                    session = liveSession,
                    onAnswer = viewModel::submitAnswer,
                    onNext = viewModel::advanceQuestion,
                    onExit = {
                        viewModel.endSession()
                        navController.popBackStack(Routes.Home, inclusive = false)
                    }
                )
            }
        }
    }
}
