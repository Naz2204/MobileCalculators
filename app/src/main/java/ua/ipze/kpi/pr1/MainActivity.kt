package ua.ipze.kpi.pr1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "input/fuel") {
        composable("input/fuel") {
            FuelInputScreen(navController)
        }
        composable ("input/oil") {
            OilInputScreen(navController)
        }
        composable(
            "result/fuel/{value}",
            arguments = listOf(navArgument("value") { type = NavType.StringType })
        ) { backStackEntry ->
            val result = backStackEntry.arguments?.getString("value") ?: "[]"
            val array = Json.decodeFromString<Map<String, JsonElement>>(result)
            ResultScreenFuel(result = array, navController = navController)
        }
        composable(
            "result/oil/{value}",
            arguments = listOf(navArgument("value") { type = NavType.StringType })
        ) { backStackEntry ->
            val result = backStackEntry.arguments?.getString("value") ?: "[]"
            val array = Json.decodeFromString<Map<String, JsonElement>>(result)
            ResultScreenOil(result = array, navController = navController)
        }
    }
}


