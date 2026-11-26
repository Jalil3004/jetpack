package com.example.jetpackandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                EventListScreen(events = events)
            }
        }
    }
}

data class Event(
    val title: String,
    val category: String,
    val location: String?,
    val capacity: Int
)

// Catalogue écrit dans le code (pas encore d'API).
private val events = listOf(
    Event(
        title = "Atelier Jetpack Compose",
        category = "Atelier",
        location = "Salle B204",
        capacity = 24
    ),
    Event(
        title = "Conférence de rentrée",
        category = "Conférence",
        location = "Amphithéâtre A",
        capacity = 180
    ),
    // Sans lieu : la carte affiche « Lieu à confirmer ».
    Event(
        title = "Tournoi de futsal",
        category = "Sport",
        location = null,
        capacity = 40
    ),
    // Complet : le bouton « Réserver » est désactivé.
    Event(
        title = "Soirée d'intégration",
        category = "Soirée",
        location = "Foyer étudiant",
        capacity = 0
    ),
    Event(
        title = "Forum des associations",
        category = "Vie étudiante",
        location = "Hall principal",
        capacity = 300
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(events: List<Event>, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(text = "Campus") }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events) { event ->
                EventCard(
                    event = event,
                    onClick = { Log.d(TAG, "Clic sur la carte : ${event.title}") },
                    onBook = { Log.d(TAG, "Réservation : ${event.title}") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventListScreenPreview() {
    JetpackAndroidTheme {
        EventListScreen(events = events)
    }
}

@Composable
fun EventHeader(
    title: String,
    category: String,
    location: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = category, style = MaterialTheme.typography.labelLarge)
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = location ?: "Lieu à confirmer",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventHeaderPreview() {
    JetpackAndroidTheme {
        EventHeader(
            title = "Atelier Jetpack Compose",
            category = "Atelier",
            location = null
        )
    }
}

// Stateless : la carte ne reçoit que des données (event) et des callbacks.
@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit,
    onBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Étape 1 : le titre prend la place restante (weight) et passe à la ligne,
            // la catégorie est mesurée en premier et reste donc toujours visible.
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = event.category,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            // Étape 2 : lieu, ou texte de remplacement s'il est null.
            Text(
                text = event.location ?: "Lieu à confirmer",
                style = MaterialTheme.typography.bodyMedium
            )
            // Actions alignées à droite dans la Column : « Voir » reprend le onClick
            // de la carte, « Réserver » est désactivé quand l'événement est complet.
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onClick) {
                    Text(text = "Voir")
                }
                Button(onClick = onBook, enabled = event.capacity > 0) {
                    Text(text = "Réserver")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardLongTitlePreview() {
    JetpackAndroidTheme {
        EventCard(
            event = Event(
                // 80 caractères
                title = "Conférence de rentrée : essayer Jetpack Compose et créer sa première application",
                category = "Conférence",
                location = "Amphithéâtre A",
                capacity = 180
            ),
            onClick = {},
            onBook = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardNoLocationPreview() {
    JetpackAndroidTheme {
        EventCard(
            event = Event(
                title = "Atelier Jetpack Compose",
                category = "Atelier",
                location = null,
                capacity = 24
            ),
            onClick = {},
            onBook = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardFullPreview() {
    JetpackAndroidTheme {
        EventCard(
            event = Event(
                title = "Soirée d'intégration",
                category = "Soirée",
                location = "Foyer étudiant",
                capacity = 0
            ),
            onClick = {},
            onBook = {},
            modifier = Modifier.padding(16.dp)
        )
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