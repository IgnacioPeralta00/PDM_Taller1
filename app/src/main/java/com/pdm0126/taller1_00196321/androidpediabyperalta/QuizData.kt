package com.pdm0126.taller1_00196321.androidpediabyperalta

val quizQuestions = listOf(
    Question(
        id = 1,
        question = "¿Cuál es la base del sistema operativo Android?",
        options = listOf(
            "Windows Kernel",
            "Linux Kernel",
            "Unix Puro",
            "Java Virtual Machine"
        ),
        correctAnswer = "Linux Kernel",
        funFact = "Linux no solo es la base de Android, sino también de la mayoría de servidores web, sistemas en la nube y supercomputadoras."
    ),
    Question(
        id = 2,
        question = "¿Qué versión introdujo Material Design?",
        options = listOf(
            "KitKat",
            "Jelly Bean",
            "Lollipop",
            "Nougat",
        ),
        correctAnswer = "Lollipop",
        funFact = "Material Design no solo redefinió la interfaz de Android con animaciones fluidas y capas visuales basadas en “materiales”, sino que se convirtió en un sistema de diseño unificado usado en apps web"
    ),
    Question(
        id = 3,
        question = "¿Qué versión introdujo la batería adaptativa basada en IA?",
        options = listOf(
            "Oreo",
            "Pie",
            "Nougat",
            "Marshmallow"
        ),
        correctAnswer = "Pie",
        funFact = "La batería adaptativa de Android Pie utiliza aprendizaje automático para identificar qué apps usas con más frecuencia y limitar las que no usas, reduciendo el consumo en segundo plano y prolongando significativamente la duración"
    )
)