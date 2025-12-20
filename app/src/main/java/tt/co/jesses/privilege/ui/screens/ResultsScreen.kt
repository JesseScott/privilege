package tt.co.jesses.privilege.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tt.co.jesses.privilege.R
import tt.co.jesses.privilege.ui.viewmodel.QuestionnaireViewModel

@Composable
fun ResultsScreen(
    viewModel: QuestionnaireViewModel
) {
    val questions = viewModel.questions
    val answers by viewModel.answers.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = stringResource(R.string.results_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(questions) { question ->
                    val answer = answers[question.id]
                    val answerText = when (answer) {
                        true -> stringResource(R.string.action_yes)
                        false -> stringResource(R.string.action_no)
                        else -> stringResource(R.string.result_answer_unanswered)
                    }
                    val itemContentDescription = stringResource(R.string.cd_result_item, question.text, answerText)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .semantics(mergeDescendants = true) {
                                contentDescription = itemContentDescription
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = question.text,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Text(
                                    text = stringResource(R.string.result_answer_label),
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Text(
                                    text = answerText,
                                    fontWeight = FontWeight.Bold,
                                    color = when (answer) {
                                        true -> MaterialTheme.colorScheme.primary
                                        false -> MaterialTheme.colorScheme.error
                                        else -> MaterialTheme.colorScheme.outline
                                    }
                                )
                            }
                        }
                    }
                }
            }

            TextButton(
                onClick = { viewModel.resetQuestionnaire() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.action_reset))
            }
        }
    }
}
