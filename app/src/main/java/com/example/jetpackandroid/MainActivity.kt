package com.example.jetpackandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpackandroid.ui.theme.JetpackAndroidTheme

private const val TAG = "Campus"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Diagnostic :
        // 1. val capacity: Int = "12"
        //    -> erreur de COMPILATION (fenêtre Build : « Initializer type mismatch:
        //       expected 'Int', actual 'String' »). Le build échoue, l'application
        //       ne s'installe pas.
        // 2. val capacity = "douze".toInt()
        //    -> erreur d'EXÉCUTION (crash au lancement, visible dans Logcat) :
        //       java.lang.NumberFormatException: For input string: "douze",
        //       levée dans MainActivity.onCreate. L'application s'installe mais plante.
        // 3. Correction : toIntOrNull() renvoie null au lieu de planter, ?: 0 fournit
        //    la valeur par défaut.
        val capacity = "douze".toIntOrNull() ?: 0
        Log.d(TAG, "Places : $capacity")
        setContent {
            JetpackAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackAndroidTheme {
        Greeting("Android")
    }
}