pipeline {
    agent any

    environment {
        GRADLE_HOME = "c:/Gradle"  // Ajusta según la ruta de instalación de Gradle en tu sistema
        PATH = "$GRADLE_HOME/bin:$PATH"
        TAGS = ''
    }

    stages {
        // Stage para obtener el código desde GitHub
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/MalvadoJoex/Prueba2.git'
            }
        }
        
        // Stage para instalar las dependencias y preparar el entorno
        // stage('Install Dependencies') {
        //     steps {
        //         script {
        //             // Limpia el proyecto y construye el entorno con las dependencias necesarias
        //             bat './gradlew clean build'
        //         }
        //     }
        // }

        stage('Detectar Cambios en las Pruebas') {
            steps {
                script {
                    def TAGS = []
                    // Obtén los archivos cambiados en el commit más reciente
                    def changedFiles = bat(script: 'git diff --name-only HEAD~1 HEAD', returnStdout: true).trim().split('\n')
                    // Filtra los archivos que son pruebas y que contienen tags
                    changedFiles.each { file ->
                        if (file.contains(".feature")) {
                            // Extraer los tags dentro de los archivos modificados
                            def tagsInFile = bat(script: "findstr -o '@tag[0-9]' ${file}", returnStdout: true).trim()
                            if (tagsInFile) {
                                TAGS += tagsInFile + " "
                            }
                        }
                    }
                    echo "Tags a ejecutar: ${TAGS}"
                }
            }
        }
        stage('Ejecutar Pruebas Modificadas') {
            steps {
                script {
                    if (TAGS) {
                        // Ejecuta solo las pruebas etiquetadas que fueron modificadas
                        bat "mvn test -Dcucumber.filter.tags='${TAGS}'"
                    } else {
                        echo "No hay pruebas modificadas para ejecutar."
                    }
                }
            }
        }
        stage('Generar Reporte HTML') {
            steps {
                // Genera el reporte HTML de las pruebas ejecutadas
                archiveArtifacts allowEmptyArchive: true, artifacts: '**/target/*.html'
            }
        }
    }

    post {
        success {
            script {
                // Subir el reporte y actualizar el estado en Jira
                bat """
                curl -D- -u joejobatua9000@gmail.com:ATATT3xFfGF0C97uY4I3i9GwuAZf_inuq7DKQZY9KAeZQjy07D7ibatGxJ5TCKgjD6pEjAr7UQIsYTueQNR4djp0oQrVM2aD7DN2hzvbXRM_pKAmWw2cQKS9hVhxfFyCX7UyQB6ssGuERwrPnfRLkEPJv9Mymrdstm_4ta66kDa0sdS_o4YyULEQ=899ED88F -X POST -H "Content-Type: application/json" \
                -d '{"transition": {"id": "31"}}' \
https://pruebasekt.atlassian.net/rest/api/2/issue/${env.JIRA_TICKET}/transitions
                """
            }
        }
        failure {
            script {
                // Crear un caso en Jira para los errores
                def response = bat(script: """
                curl -D- -u joejobatua9000@gmail.com:ATATT3xFfGF0C97uY4I3i9GwuAZf_inuq7DKQZY9KAeZQjy07D7ibatGxJ5TCKgjD6pEjAr7UQIsYTueQNR4djp0oQrVM2aD7DN2hzvbXRM_pKAmWw2cQKS9hVhxfFyCX7UyQB6ssGuERwrPnfRLkEPJv9Mymrdstm_4ta66kDa0sdS_o4YyULEQ=899ED88F -X POST -H "Content-Type: application/json" \
                -d '{
                    "fields": {
                        "project": {
                            "key": "PROYECTO"
                        },
                        "summary": "Falla en pruebas automatizadas: ${TAGS}",
                        "description": "Las pruebas asociadas con los tags ${TAGS} han fallado.",
                        "issuetype": {
                            "name": "Bug"
                        }
                    }
                }'
https://pruebasekt.atlassian.net/rest/api/2/issue/
                """, returnStdout: true)
                // Subir el reporte HTML al ticket de Jira
                def issueKey = (response =~ /"key":"(.*?)"/)[0][1]
                echo "Ticket creado en Jira: ${issueKey}"
                bat """
                curl -D- -u joejobatua9000@gmail.com:ATATT3xFfGF0C97uY4I3i9GwuAZf_inuq7DKQZY9KAeZQjy07D7ibatGxJ5TCKgjD6pEjAr7UQIsYTueQNR4djp0oQrVM2aD7DN2hzvbXRM_pKAmWw2cQKS9hVhxfFyCX7UyQB6ssGuERwrPnfRLkEPJv9Mymrdstm_4ta66kDa0sdS_o4YyULEQ=899ED88F -X POST -H "X-Atlassian-Token: no-check" \
                -F "file=@target/reporte.html" \
                -F "name=reporte.html" \
                -F "description=Reporte de pruebas fallidas" \
https://pruebasekt.atlassian.net/rest/api/2/issue/${issueKey}/attachments
                """
            }
        }
    }
}
        