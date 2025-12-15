package tt.co.jesses.privilege.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import tt.co.jesses.privilege.ui.viewmodel.QuestionnaireViewModel

@Composable
fun QuestionnaireScreen(
    viewModel: QuestionnaireViewModel,
    onFinished: () -> Unit
) {
    val questions = viewModel.questions
    val answers by viewModel.answers.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { questions.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            LinearProgressIndicator(
                progress = {
                    if (questions.isNotEmpty()) {
                        (pagerState.currentPage + 1).toFloat() / questions.size
                    } else {
                        0f
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                val question = questions[page]
                val selectedAnswer = answers[question.id]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = question.text,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 48.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                viewModel.submitAnswer(question.id, true)
                                if (page < questions.size - 1) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(page + 1)
                                    }
                                } else {
                                    viewModel.completeQuestionnaire()
                                    onFinished()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedAnswer == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = if (selectedAnswer == true) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Yes")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = {
                                viewModel.submitAnswer(question.id, false)
                                if (page < questions.size - 1) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(page + 1)
                                    }
                                } else {
                                    viewModel.completeQuestionnaire()
                                    onFinished()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedAnswer == false) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = if (selectedAnswer == false) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("No")
                        }
                    }
                }
            }
        }
    }
}
