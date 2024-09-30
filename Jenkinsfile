pipeline {
    agent any

    environment {
        GRADLE_HOME = "/usr/share/gradle"  // Ajusta según la ruta de instalación de Gradle en tu sistema
        PATH = "$GRADLE_HOME/bin:$PATH"
    }

    stages {
        // Stage para obtener el código desde GitHub
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/MalvadoJoex/Prueba2.git'
            }
        }
        
        // Stage para instalar las dependencias y preparar el entorno
        stage('Install Dependencies') {
            steps {
                script {
                    // Limpia el proyecto y construye el entorno con las dependencias necesarias
                    sh './gradlew clean build'
                }
            }
        }

        // Stage para ejecutar las pruebas automáticas con los tags correspondientes
        stage('Run Tests') {
            steps {
                script {
                    // Detecta los tags modificados o añadidos y ejecuta las pruebas relacionadas
                    sh './gradlew test -Dcucumber.filter.tags="@tag4 or @DesafioCasa or @TodoTDD_TDC or @TodoMisPedidos or @TodoAyuda"'
                }
            }
        }
    }

    post {
        // Siempre publica los reportes y resultados, independientemente de si las pruebas pasan o fallan
        always {
            // Publica los resultados en formato JUnit para que Jenkins los pueda interpretar
            junit 'build/test-results/**/*.xml'

            // Publica los reportes de Cucumber (si los tienes configurados en tu Runner)
            cucumber 'build/reports/cucumber/*.json'
        }

        // Opción para limpiar el entorno después de la ejecución de pruebas
        cleanup {
            cleanWs()
        }
    }
}