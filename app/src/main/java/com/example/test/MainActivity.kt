package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.test.navigation.navigateForResultFeature
import com.example.test.navigation.presenterNavigationRoute
import com.example.test.ui.theme.TestProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestProjectTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = presenterNavigationRoute) {
                    navigateForResultFeature(navController = navController)
                }
            }
        }
    }
}

inline fun <reified R> R.serialize(): String = Json.encodeToString(this)

inline fun <reified R> String.deserialize(): R = Json.decodeFromString(this)