pipeline {
    agent any
    
    environment {
        // Variables de entorno para Jira y el directorio de reportes
        JIRA_URL = 'https://pruebasekt.atlassian.net'
        REPORTS_DIR = "build/reports"
        PDF_REPORT = "${REPORTS_DIR}/report.pdf"
        HTML_REPORT = "${REPORTS_DIR}/report.html"
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Clonar el repositorio desde GitHub
                git branch: 'main', url: 'https://github.com/MalvadoJoex/Prueba2.git'
            }
        }
        
        stage('Detectar archivos modificados') {
            steps {
                script {
                    def featureFiles = []
                    // Detectar archivos .feature modificados
                    def changedFiles = bat(script: "git diff --name-only HEAD~1", returnStdout: true).trim().split('\n')
                    featureFiles = changedFiles.findAll { it.endsWith('.feature') }
                    
                    if (featureFiles.isEmpty()) {
                        echo "No se encontraron archivos .feature modificados."
                        currentBuild.result = 'SUCCESS'
                        return
                    }
                    
                    echo "Archivos .feature modificados: ${featureFiles}"
                }
            }
        }
        
        // stage('Extraer tags y ejecutar pruebas') {
        //     steps {
        //         script {
        //             // Extraer los tags que comiencen con @ de los archivos modificados
        //              def featureFiles = []
        //             featureFiles.each { featureFile ->
        //                 def tags = bat(script: "findstr /r '@[^ ]*' ${featureFile}", returnStdout: true).trim().split('\n')
        //                 echo "Tags encontrados en ${featureFile}: ${tags}"
                        
        //                 // Ejecutar pruebas con los tags encontrados
        //                 if (tags) {
        //                     def tagList = tags.join(' or ')
        //                     bat "gradle clean test -Dcucumber.options='--tags \"${tagList}\"'"
        //                 }
        //             }
        //         }
        //     }
        // }

        // stage('Extraer tags y ejecutar pruebas') {
        //     steps {
        //         script {
        //             // Extraer los tags que comiencen con @ de los archivos modificados
        //             def featureFiles = []
        //             // Repetir la captura de archivos .feature para este contexto
        //             def changedFiles = bat(script: "git diff --name-only HEAD~1", returnStdout: true).trim().split('\n')
        //             featureFiles = changedFiles.findAll { it.endsWith('.feature') }

        //             featureFiles.each { featureFile ->
        //             def fileName = new File(featureFile).getName()
        //                 // bat(script: "cd src/test/resources/features")
        //                 // def tags = bat(script: "findstr /r \"@.*\" ${featureFile}", returnStdout: true).trim().split('\n')
        //                 // echo "Tags encontrados en ${featureFile}: ${tags}"
        //                  def tags = bat(script: """
        //                 cd src/test/resources/features
        //                 findstr /r "@.*" ${fileName}
        //             """, returnStdout: true).trim().split('\n')

        //             echo "Tags encontrados en ${fileName}: ${tags}"
                        
        //                 // Ejecutar pruebas con los tags encontrados
        //                 if (tags) {
        //                     def tagList = tags.join(' or ')
        //                     sh "gradle clean test -Dcucumber.options='--tags \"${tagList}\"'"
        //                 }
        //             }
        //         }
        //     }
        // }

        stage('Extraer tags y ejecutar pruebas') {
    steps {
        script {
            // Extraer los tags que comiencen con @ de los archivos modificados
            def featureFiles = []
            // Obtener archivos modificados
            def changedFiles = bat(script: "git diff --name-only HEAD~1", returnStdout: true).trim().split('\n')
            // Filtrar solo los archivos .feature
            featureFiles = changedFiles.findAll { it.endsWith('.feature') }

            featureFiles.each { featureFile ->
                // Extraer solo el nombre del archivo
                def fileName = featureFile.tokenize('/').last() // Esto toma solo el nombre del archivo
                
                // Cambiar al directorio donde se encuentran los archivos .feature
                dir('src/test/resources/features') {
                    // Usar findstr para extraer los tags
                    def tags = bat(script: "findstr /r \"@.*\" ${fileName}", returnStdout: true).trim().split('\n')

                    echo "Tags encontrados en ${fileName}: ${tags.join(', ')}"

                    // Ejecutar pruebas con los tags encontrados
                    if (tags) {
                        def tagList = tags.collect { it.trim() }.join(' or ') // Mantener el símbolo @
                        dir('github-webhook'){
                            bat "gradle clean test -Dcucumber.options='--tags \"${tagList}\"'"
                        }
                    }
                }
            }
        }
    }
}
        
        stage('Generar reportes') {
            steps {
                script {
                    // Generar los reportes en PDF y HTML
                    echo "Generando reportes HTML y PDF..."
                    bat "gradle generateReport"
                    
                    // Archivar los reportes
                    archiveArtifacts artifacts: "${HTML_REPORT}, ${PDF_REPORT}", allowEmptyArchive: false
                }
            }
        }
        
        stage('Crear caso en Jira') {
            steps {
                script {
                    // Crear un nuevo caso en Jira
                    jiraNewIssue site: 'JiraSite', projectKey: 'PRUEB', issueType: 'Bug', summary: 'Resultados de pruebas automatizadas', description: 'Las pruebas automatizadas se ejecutaron correctamente. Ver adjuntos para más detalles.', priority: 'Major'
                    
                    // Adjuntar los reportes en HTML y PDF
                    jiraAttachFiles idOrKey: jiraIssueKey, files: [HTML_REPORT, PDF_REPORT]
                }
            }
        }
        
        stage('Actualizar estado de Jira') {
            steps {
                script {
                    def testResult = currentBuild.result ?: 'SUCCESS'
                    
                    // Actualizar el estado en Jira dependiendo del resultado
                    if (testResult == 'SUCCESS') {
                        jiraTransitionIssue idOrKey: jiraIssueKey, transitionName: 'Done'
                    } else {
                        jiraTransitionIssue idOrKey: jiraIssueKey, transitionName: 'Reopen'
                    }
                }
            }
        }
    }
    
    post {
        always {
            // Limpiar workspace
            cleanWs()
        }
    }
}