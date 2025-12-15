package tt.co.jesses.privilege

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tt.co.jesses.privilege.ui.screens.QuestionnaireScreen
import tt.co.jesses.privilege.ui.screens.ResultsScreen
import tt.co.jesses.privilege.ui.screens.WelcomeScreen
import tt.co.jesses.privilege.ui.theme.PrivilegeTheme
import tt.co.jesses.privilege.ui.viewmodel.QuestionnaireViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrivilegeTheme {
                PrivilegeApp()
            }
        }
    }
}

@Composable
fun PrivilegeApp() {
    val navController = rememberNavController()
    val viewModel: QuestionnaireViewModel = hiltViewModel()
    val isChecklistFilled by viewModel.isPrivilegeChecklistFilled.collectAsStateWithLifecycle()

    // We can use a LaunchEffect or simply the start destination logic.
    // However, since isChecklistFilled is async, it might be false initially and then true.
    // A better approach for a robust app is to have a "Loading" state or splash.
    // For now, we will observe the state. If it changes to true, we navigate to Results.
    // If we are on Welcome and it is true, we go to Results.

    // Simplest approach: Use "Welcome" as start, but if `isChecklistFilled` is true,
    // the UI naturally updates or we navigate.

    // Let's rely on Navigation.
    // We can pass the startDestination dynamically if we wait for the first emission,
    // but that blocks UI.
    // Instead, let's just default to Welcome. In the NavHost, we can check.

    // Better: Observe the state here. If true, show ResultsScreen directly (skipping NavHost logic if we want, or navigating).
    // But the requirement is a questionnaire flow.

    if (isChecklistFilled) {
        ResultsScreen(viewModel = viewModel)
    } else {
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onStartClicked = {
                        navController.navigate("questionnaire")
                    }
                )
            }
            composable("questionnaire") {
                QuestionnaireScreen(
                    viewModel = viewModel,
                    onFinished = {
                        // The ViewModel updates the preference, which updates isChecklistFilled.
                        // The top-level if(isChecklistFilled) will recompose and show ResultsScreen.
                        // So we might not strictly need to navigate, but it's good practice to clear backstack if we were doing it inside NavHost.
                        // Here, the recomposition will switch the whole tree to ResultsScreen.
                    }
                )
            }
        }
    }
}
