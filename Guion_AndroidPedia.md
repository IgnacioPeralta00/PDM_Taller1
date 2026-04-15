# Guion Detallado de Flujo: AndroidPedia

Este documento describe el flujo lógico y la implementación técnica de la aplicación **AndroidPedia**, con un desglose línea por línea del componente principal de juego.

---

### 1. El Cerebro: `AndroidPediaByPeralta`
Este componente es el que decide qué se muestra en pantalla. No dibuja botones directamente, sino que gestiona el "estado".

*   **`screen`**: Una variable que dice si estamos en "welcome", "quiz" o "results".
*   **`currentIndex`**: El número de la pregunta actual (0, 1 o 2).
*   **`score`**: Cuántas preguntas ha acertado el usuario.
*   **Lógica**: Usa un `when(screen)` para intercambiar las pantallas. Es como un proyector que cambia de diapositiva cuando tú presionas un botón.

---

### 2. La Bienvenida: `Welcome`
Es una función simple que muestra el título, subtítulo y tus datos. Recibe una orden: `onStartQuiz`. Al pulsar el botón, esta orden cambia el estado `screen` a `"quiz"`, y la app se transforma automáticamente.

---

### 3. El Motor: `Quiz` (Explicación Línea por Línea)
Aquí es donde ocurre la magia. Vamos a desglosar el bloque de las opciones que es el más importante:

#### A. Generando las opciones
```kotlin
currentQuestion.options.forEach { option ->
```
*   **Explicación**: Por cada una de las 4 opciones de la pregunta actual, ejecuta el código que sigue. La variable `option` guarda el texto de esa opción (ej. "Linux Kernel").

#### B. El color del botón (Lógica Visual)
```kotlin
val containerColor = when {
    !isAnswered -> MaterialTheme.colorScheme.primaryContainer // Si no has respondido, usa el color morado/azul normal.
    option == currentQuestion.correctAnswer -> Color.Green     // Si ya respondiste, la opción que sea la correcta SIEMPRE se pinta de verde.
    option == selectedOption -> Color.Red                      // Si la opción que tocaste NO era la correcta, se pinta de rojo.
    else -> MaterialTheme.colorScheme.surfaceVariant           // El resto de opciones se quedan en un gris neutro.
}
```

#### C. El color del texto (Legibilidad)
```kotlin
val contentColor = if (isAnswered && (option == currentQuestion.correctAnswer || option == selectedOption))
    Color.White else MaterialTheme.colorScheme.onSurfaceVariant
```
*   **Explicación**: Si ya se respondió y este botón es el que está en Verde o Rojo, pon el texto blanco para que resalte. Si no, déjalo en el color oscuro por defecto.

#### D. El Botón y su comportamiento
```kotlin
Button(
    onClick = {
        if (!isAnswered) {               // Solo permite hacer clic si aún no has respondido.
            selectedOption = option      // Guarda cuál tocaste.
            isAnswered = true            // Marca la pregunta como "respondida" (bloquea otros clics).
            if (option == currentQuestion.correctAnswer) onAnswerCorrect() // Si acertaste, sube el puntaje global.
        }
    },
    enabled = !isAnswered || option == selectedOption || option == currentQuestion.correctAnswer,
```
*   **`enabled`**: Esta línea es clave. Una vez que respondes, solo se mantienen "encendidos" visualmente el botón correcto y el que tú elegiste. Los demás se ven desactivados.

#### E. Forzando los colores
```kotlin
    colors = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = containerColor, // IMPORTANTE: Aunque el botón esté desactivado, queremos que siga viéndose Verde o Rojo.
        disabledContentColor = contentColor
    )
```

#### F. El Feedback Final
```kotlin
if (isAnswered) {
    FunFact(...) // Solo aparece cuando 'isAnswered' es verdadero.
    Button(...) { // El botón para avanzar que dice "Siguiente" o "Ver Resultado" según la pregunta.
        Text(text = if (currentIndex < 2) "Siguiente" else "Ver Resultado")
    }
}
```

---

### 4. El Final: `ResultScreen`
Cuando el índice de preguntas llega al final, pasamos aquí.

*   **`when(score)`**: Revisa cuántos puntos hiciste y elige una frase: "¡Excelente!", "Muy bien" o "Sigue intentando".
*   **Reinicio**: El botón "Reiniciar" pone el `score` en 0, el `currentIndex` en 0 y nos manda de vuelta a la pantalla `"welcome"`. Todo el ciclo vuelve a empezar.

---
**Resumen Técnico**: La app funciona como una **máquina de estados**. No hay saltos entre archivos, solo variables que cambian (`remember`) y una interfaz que reacciona a esos cambios pintándose de diferentes colores o mostrando nuevos elementos.
