package ua.ipze.kpi.pr6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "input") {
        composable("input") {
            CardListScreen(navController)
        }
        composable("result/{value}",
            arguments = listOf(navArgument("value") { type = NavType.StringType })
        ) { backStackEntry ->
            val result = backStackEntry.arguments?.getString("value") ?: "[]"
            val array = Json.decodeFromString<Map<String, JsonElement>>(result)
            ResultScreen(result = array, navController = navController)
        }
    }
}