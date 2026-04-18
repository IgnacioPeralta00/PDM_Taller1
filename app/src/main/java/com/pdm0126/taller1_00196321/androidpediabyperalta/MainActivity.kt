package com.pdm0126.taller1_00196321.androidpediabyperalta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm0126.taller1_00196321.androidpediabyperalta.ui.theme.AndroidPediaByPeraltaTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPediaByPeraltaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(R.string.app_name),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    AndroidPediaByPeralta(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AndroidPediaByPeralta(modifier: Modifier = Modifier) {
    // Observación (Criterio 1): ¿Por qué usar Strings planos ("welcome", "quiz", "results") para manejar el enrutamiento?
    // Esto puede causar un bug si se escribe mal el String en alguna parte. Podría considerar usar un Enum o una Sealed Class para hacer el estado más seguro.
    var screen by remember { mutableStateOf("welcome") }
    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }

    // Observación (Criterio 5): Estás aplicando State Hoisting de maravilla aquí.
    // Mantener estos estados en la raíz y pasarlos como lambdas (onStartQuiz, onAnswerCorrect) a los hijos hace que los componentes sean puros y testeables.
    when (screen) {
        "welcome" -> Welcome(
            modifier = modifier,
            onStartQuiz = { screen = "quiz" }
        )
        "quiz" -> Quiz(
            modifier = modifier,
            currentIndex = currentIndex,
            score = score,
            onAnswerCorrect = { score++ },
            // Sugerencia (Criterio 3): Esta lógica de evaluación de límites podría delegarse a una función separada en lugar de vivir directamente en la lambda de la UI.
            onNextQuestion = {
                if (currentIndex < quizQuestions.size - 1) {
                    currentIndex++
                } else {
                    screen = "results"
                }
            }
        )
        "results" -> ResultScreen(
            modifier = modifier,
            score = score,
            onRestart = {
                currentIndex = 0
                score = 0
                screen = "welcome"
            }
        )
    }
}

@Composable
fun Welcome(modifier: Modifier = Modifier, onStartQuiz: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AndroidPedia",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "¿Cuánto sabes de Android?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Fidel Ignacio Peralta Rivas\n00196321",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onStartQuiz,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(text = "Comenzar Quiz")
        }
    }
}

@Composable
fun ProgressAndScore(
    progress: Int,
    score: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Pregunta $progress de 3",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "Puntaje: $score / 3",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Quiz(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    score: Int,
    onAnswerCorrect: () -> Unit,
    onNextQuestion: () -> Unit
) {
    val currentQuestion = quizQuestions[currentIndex]
    // Buen trabajo (Criterio 2): ¡Excelente uso de la llave 'currentIndex' en el remember!
    // Esto garantiza perfectamente que selectedOption e isAnswered se limpien automáticamente cada vez que pasas a una nueva pregunta.
    var selectedOption by remember(currentIndex) { mutableStateOf<String?>(null) }
    var isAnswered by remember(currentIndex) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProgressAndScore(
            progress = currentIndex + 1,
            score = score
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = currentQuestion.question,
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Reflexión (Criterio 3): ¿Por qué calcular el color del contenedor y del contenido directamente dentro del forEach y la UI?
            // Podría considerarse mover esta lógica condicional de colores a una función externa que simplemente reciba los estados y retorne el color adecuado, limpiando así la vista.
            currentQuestion.options.forEach { option ->
                val containerColor = when {
                    !isAnswered -> MaterialTheme.colorScheme.primaryContainer
                    option == currentQuestion.correctAnswer -> colorResource(R.color.light_green)
                    option == selectedOption -> colorResource(R.color.light_red)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }

                val contentColor = if (isAnswered && (option == currentQuestion.correctAnswer || option == selectedOption))
                    Color.White else MaterialTheme.colorScheme.onSurfaceVariant

                // Sugerencia (Criterio 4): Este Button tiene bastantes configuraciones (colores, condicionales de enabled, lógica en el onClick).
                // ¿Podrías considerarse extraer este botón completo a un Composable reutilizable independiente (por ejemplo, `QuizOptionButton`)?
                Button(
                    // Reflexión (Criterio 3): ¿Es necesario tener el bloque if y la asignación de tres variables de estado dentro del onClick?
                    // Extraer esto a una función de manejo de eventos haría que la lectura de la UI fuera más fluida.
                    onClick = {
                        if (!isAnswered) {
                            selectedOption = option
                            isAnswered = true
                            if (option == currentQuestion.correctAnswer) onAnswerCorrect()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isAnswered || option == selectedOption || option == currentQuestion.correctAnswer,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = containerColor,
                        contentColor = contentColor,
                        disabledContainerColor = containerColor,
                        disabledContentColor = contentColor
                    )
                ) {
                    Text(text = option)
                }
            }
        }

        if (isAnswered) {
            FunFact(funFact = currentQuestion.funFact)
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = onNextQuestion,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = if (currentIndex < quizQuestions.size - 1) "Siguiente" else "Ver Resultado")
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun FunFact(funFact: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Dato curioso...",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = funFact,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    score: Int,
    onRestart: () -> Unit
) {
    val message = when (score) {
        3 -> "¡Excelente! Eres todo un Androide."
        2 -> "¡Muy bien! Tienes buenos conocimientos."
        1 -> "¡Sigue intentando! Puedes aprender más."
        else -> "Parece que necesitas estudiar un poco más."
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Resultado Final",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Obtuviste $score de 3",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onRestart) {
            Text(text = "Reiniciar Quiz")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    AndroidPediaByPeraltaTheme {
        AndroidPediaByPeralta()
    }
}
