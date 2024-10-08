pipeline {
    agent any
    
    environment {
        // Variables de entorno para Jira y el directorio de reportes
        JIRA_URL = 'https://pruebasekt.atlassian.net'
        jiraSite = 'PruebaEmpresa'
        REPORTS_DIR = "ExtentReports/SparkReport_*"
        PDF_REPORT = "${REPORTS_DIR}/HtmlReport/ExtentHtml.html"
        HTML_REPORT = "${REPORTS_DIR}/PdfReport/ExtentPdf.pdf"
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
                dir('src/test/resources') {
                    // Usar findstr para extraer los tags
                    def tagsOutput = bat(script: "findstr /r \"@.*\" ${fileName}", returnStdout: true).trim()

                    // Separar por líneas y filtrar los tags
                    def tags = []
                    tagsOutput.tokenize('\n').each { line ->
                        def matchedTags = line.tokenize().findAll { it.startsWith('@') }
                        tags.addAll(matchedTags)
                    }

                    echo "Tags encontrados en ${fileName}: ${tags.join(', ')}"

                    // Ejecutar pruebas con los tags encontrados
                    if (tags) {
                        def tagList = tags.join(' or ') // Crear una lista de tags separados por " or "
                        
                        // Cambiar al directorio raíz para ejecutar Gradle
                        dir('../../../..') { // Regresa tres niveles a la raíz
                            bat "gradle clean test -Dcucumber.options=\"--tags '${tagList}'\""
                            //bat "gradle clean test ${tagList}"
                        }
                    }
                }
            }
        }
    }
}
        
// stage('Generar reportes') {
//     steps {
//         script {
//             // Generar los reportes en PDF y HTML
//             echo "Generando reportes HTML y PDF..."
//             bat "gradle generateReport"

//             // Listar los archivos en el directorio ExtentReports y obtener la ruta del HTML generado
//             def output = bat(script: 'for /r ExtentReports %%i in (*.html) do @echo %%i', returnStdout: true).trim()
//             echo "Archivos generados:\n${output}"

//             // Filtrar las líneas que contienen las rutas de los archivos
//             def reportPaths = output.split("\r\n").findAll { it.endsWith(".html") }
//             if (reportPaths.size() > 0) {
//                 def htmlReportPath = reportPaths[0] // Tomar el primer archivo .html encontrado
//                 echo "Archivo HTML encontrado: ${htmlReportPath}"
//                 archiveArtifacts artifacts: htmlReportPath, allowEmptyArchive: false
//             } else {
//                 echo "No se encontró archivo HTML."
//             }

//             // Repetir para PDF si es necesario
//             def pdfOutput = bat(script: 'for /r ExtentReports %%i in (*.pdf) do @echo %%i', returnStdout: true).trim()
//             echo "Archivos PDF generados:\n${pdfOutput}"

//             def pdfReportPaths = pdfOutput.split("\r\n").findAll { it.endsWith(".pdf") }
//             if (pdfReportPaths.size() > 0) {
//                 def pdfReportPath = pdfReportPaths[0]
//                 echo "Archivo PDF encontrado: ${pdfReportPath}"
//                 archiveArtifacts artifacts: pdfReportPath, allowEmptyArchive: false
//             } else {
//                 echo "No se encontró archivo PDF."
//             }
//         }
//     }
// }

stage('Generar reportes') {
    steps {
        script {
            // Generar los reportes en PDF y HTML
            echo "Generando reportes HTML y PDF..."
            bat "gradle generateReport"

            // Listar los archivos en el directorio ExtentReports y obtener la ruta del HTML generado
            def output = bat(script: 'for /r ExtentReports %%i in (*.html) do @echo %%i', returnStdout: true).trim()
            echo "Archivos generados:\n${output}"

            // Filtrar las líneas que contienen las rutas de los archivos
            def reportPaths = output.split("\r\n").findAll { it.endsWith(".html") }
            if (reportPaths.size() > 0) {
                // Usar rutas relativas
                def relativeHtmlReportPath = reportPaths[0].replace("C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\github-webhook\\", "")
                echo "Archivo HTML encontrado: ${relativeHtmlReportPath}"
                archiveArtifacts artifacts: relativeHtmlReportPath, allowEmptyArchive: false
            } else {
                echo "No se encontró archivo HTML."
            }

            // Repetir para PDF si es necesario
            def pdfOutput = bat(script: 'for /r ExtentReports %%i in (*.pdf) do @echo %%i', returnStdout: true).trim()
            echo "Archivos PDF generados:\n${pdfOutput}"

            def pdfReportPaths = pdfOutput.split("\r\n").findAll { it.endsWith(".pdf") }
            if (pdfReportPaths.size() > 0) {
                def relativePdfReportPath = pdfReportPaths[0].replace("C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\github-webhook\\", "")
                echo "Archivo PDF encontrado: ${relativePdfReportPath}"
                archiveArtifacts artifacts: relativePdfReportPath, allowEmptyArchive: false
            } else {
                echo "No se encontró archivo PDF."
            }
        }
    }
}
        
    //     stage('Crear caso en Jira') {
    //         steps {
    //             script {
    //                 // Crear un nuevo caso en Jira
    //                 jiraNewIssue site: 'pruebasekt', projectKey: 'PRUEB', issueType: 'Test', summary: 'Resultados de pruebas automatizadas', description: 'Las pruebas automatizadas se ejecutaron correctamente. Ver adjuntos para más detalles.', priority: 'Major'
                    
    //                 // Adjuntar los reportes en HTML y PDF
    //                 jiraAttachFiles idOrKey: jiraIssueKey, files: [HTML_REPORT, PDF_REPORT]
    //             }
    //         }
    //     }
        
    //     stage('Actualizar estado de Jira') {
    //         steps {
    //             script {
    //                 def testResult = currentBuild.result ?: 'SUCCESS'
                    
    //                 // Actualizar el estado en Jira dependiendo del resultado
    //                 if (testResult == 'SUCCESS') {
    //                     jiraTransitionIssue idOrKey: jiraIssueKey, transitionName: 'Done'
    //                 } else {
    //                     jiraTransitionIssue idOrKey: jiraIssueKey, transitionName: 'Reopen'
    //                 }
    //             }
    //         }
    //     }
    // }

    // stage('Crear caso en Jira') {
    //         steps {
    //             script {
    //                 // Crear un nuevo caso en Jira
    //                 def issue = jiraNewIssue site: jiraSite, 
    //                                         projectKey: 'PRUEB', 
    //                                         issueType: 'Test', 
    //                                         summary: 'Resultados de pruebas automatizadas', 
    //                                         description: 'Las pruebas automatizadas se ejecutaron correctamente. Ver adjuntos para más detalles.', 
    //                                         priority: 'Major'
                    
    //                 // Guardar el ID del issue creado
    //                 def jiraIssueKey = issue.data.key

    //                 // Adjuntar los reportes en HTML y PDF
    //                 jiraAttachFiles idOrKey: jiraIssueKey, files: [HTML_REPORT, PDF_REPORT]
    //             }
    //         }
    //     }

    //     stage('Actualizar estado de Jira') {
    //         steps {
    //             script {
    //                 def testResult = currentBuild.result ?: 'SUCCESS'
                    
    //                 // Actualizar el estado en Jira dependiendo del resultado
    //                 if (testResult == 'SUCCESS') {
    //                     jiraTransitionIssue idOrKey: jiraIssueKey, transitionName: 'Done'
    //                 } else {
    //                     jiraTransitionIssue idOrKey: jiraIssueKey, transitionName: 'Reopen'
    //                 }
    //             }
    //         }
    //     }
    // }

    //  stage('Crear caso en Jira') {
    //         steps {
    //             script {
    //                 // Definir el nuevo caso a crear en Jira
    //                 def testIssue = [
    //                     fields: [
    //                         project: [key: 'TESTEAME'], // Usa el clave del proyecto
    //                         summary: 'Resultados de pruebas automatizadas',
    //                         description: 'Las pruebas automatizadas se ejecutaron correctamente. Ver adjuntos para más detalles.',
    //                         issuetype: [name: 'Bug']//, // Asegúrate de que este tipo de issue existe
    //                         //priority: [name: 'High'] // Asegúrate de que esta prioridad existe
    //                     ]
    //                 ]

    //                 // Crear un nuevo caso en Jira
    //                 def testIssues = [issueUpdates: [testIssue]]
    //                 def response = jiraNewIssues issues: testIssues, site: jiraSite

    //                 // Comprobar el éxito de la creación
    //                 echo response.successful.toString()
    //                 def jiraIssueKey = response.data[0].key // Obtén la clave del issue creado

    //                 // Adjuntar los reportes en HTML y PDF
    //                 jiraAttachFiles idOrKey: jiraIssueKey, files: [HTML_REPORT, PDF_REPORT]
    //             }
    //         }
    //     }

    // stage('Crear caso en Jira') {
    //         steps {
    //             script {
    //                 // Datos para crear el issue en Jira
    //                 def jiraIssueInput = [
    //                     fields: [
    //                         project: [ key: 'TESTEAME' ], // Asegúrate de que el project key sea correcto
    //                         summary: 'Resultados de pruebas automatizadas',
    //                         description: 'Las pruebas automatizadas se ejecutaron correctamente. Ver adjuntos para más detalles.',
    //                         issuetype: [ name: 'Bug' ]
    //                     ]
    //                 ]

    //                 // Creación del issue en Jira
    //                 def response = jiraNewIssue issue: jiraIssueInput, site: 'PruebaEmpresa'
    //                 echo "Jira issue response: ${response}"

    //                 // Verificación del issueKey
    //                 def issueKey = response?.data?.key
    //                 if (!issueKey) {
    //                     error "No se pudo obtener el issueKey de la respuesta de Jira"
    //                 } else {
    //                     echo "IssueKey obtenido: ${issueKey}"
    //                 }

    //                 // Rutas de los archivos de reportes (Ajusta según sea necesario)
    //                 def attachFilePathHtml = '/ExtentReports/SparkReport_*/HtmlReport/ExtentHtml.html'
    //                 def attachFilePathPdf = 'ExtentReports/SparkReport_*/PdfReport/ExtentPdf.pdf'

    //                 // Validación de los archivos antes de intentar adjuntarlos
    //                 def fileHtml = new File(attachFilePathHtml)
    //                 def filePdf = new File(attachFilePathPdf)

    //                 if (fileHtml.exists() && filePdf.exists()) {
    //                     try {
    //                         // Adjuntar archivo HTML
    //                         def attachHtmlResponse = jiraAddAttachment site: 'PruebaEmpresa', issueKey: issueKey, file: fileHtml
    //                         echo "Archivo HTML adjuntado: ${attachHtmlResponse}"

    //                         // Adjuntar archivo PDF
    //                         def attachPdfResponse = jiraAddAttachment site: 'PruebaEmpresa', issueKey: issueKey, file: filePdf
    //                         echo "Archivo PDF adjuntado: ${attachPdfResponse}"

    //                     } catch (Exception e) {
    //                         error "Error adjuntando archivos: ${e.message}"
    //                     }
    //                 } else {
    //                     error "No se encontraron los archivos a adjuntar. Verifica las rutas: HTML (${attachFilePathHtml}), PDF (${attachFilePathPdf})"
    //                 }
    //             }
    //         }
    //     }

    // stage('Crear caso en Jira') {
    //         steps {
    //             script {
    //                 // Datos para crear el issue en Jira
    //                 def jiraIssueInput = [
    //                     fields: [
    //                         project: [ key: 'TESTEAME' ],
    //                         summary: 'Resultados de pruebas automatizadas',
    //                         description: 'Las pruebas automatizadas se ejecutaron correctamente. Ver adjuntos para más detalles.',
    //                         issuetype: [ name: 'Bug' ]
    //                     ]
    //                 ]

    //                 // Creación del issue en Jira
    //                 def response = jiraNewIssue issue: jiraIssueInput, site: 'PruebaEmpresa'
    //                 echo "Jira issue response: ${response}"

    //                 // Verificación del issueKey
    //                 def issueKey = response?.data?.key
    //                 if (!issueKey) {
    //                     error "No se pudo obtener el issueKey de la respuesta de Jira"
    //                 } else {
    //                     echo "IssueKey obtenido: ${issueKey}"
    //                 }

    //                 // Buscar los archivos de reportes
    //                 def attachFilePathHtml = findFiles(glob: 'ExtentReports/SparkReport_*/HtmlReport/ExtentHtml.html')
    //                 def attachFilePathPdf = findFiles(glob: 'ExtentReports/SparkReport_*/PdfReport/ExtentPdf.pdf')

    //                 // Verificar si se encontraron los archivos
    //                 if (attachFilePathHtml.isEmpty() || attachFilePathPdf.isEmpty()) {
    //                     error "No se encontraron los archivos a adjuntar. Verifica las rutas: HTML (${attachFilePathHtml}), PDF (${attachFilePathPdf})"
    //                 }

    //                 // Adjuntar los archivos en Jira
    //                 try {
    //                     attachFilePathHtml.each { file ->
    //                         def attachHtmlResponse = jiraAddAttachment site: 'PruebaEmpresa', issueKey: issueKey, file: file
    //                         echo "Archivo HTML adjuntado: ${attachHtmlResponse}"
    //                     }

    //                     attachFilePathPdf.each { file ->
    //                         def attachPdfResponse = jiraAddAttachment site: 'PruebaEmpresa', issueKey: issueKey, file: file
    //                         echo "Archivo PDF adjuntado: ${attachPdfResponse}"
    //                     }

    //                 } catch (Exception e) {
    //                     error "Error adjuntando archivos: ${e.message}"
    //                 }
    //             }
    //         }
    //     }

    // stage('Crear caso en Jira') {
    //         steps {
    //             script {
    //                 // Datos para crear el issue en Jira
    //                 def jiraIssueInput = [
    //                     fields: [
    //                         project: [ key: 'TESTEAME' ],
    //                         summary: 'Resultados de pruebas automatizadas',
    //                         description: 'Las pruebas automatizadas se ejecutaron correctamente. Ver adjuntos para más detalles.',
    //                         issuetype: [ name: 'Bug' ]
    //                     ]
    //                 ]

    //                 // Creación del issue en Jira
    //                 def response = jiraNewIssue issue: jiraIssueInput, site: 'PruebaEmpresa'
    //                 echo "Jira issue response: ${response}"

    //                 // Verificación del issueKey
    //                 def issueKey = response?.data?.key
    //                 if (!issueKey) {
    //                     error "No se pudo obtener el issueKey de la respuesta de Jira"
    //                 } else {
    //                     echo "IssueKey obtenido: ${issueKey}"
    //                 }

    //                 // Rutas de los archivos de reportes
    //                 def attachFilePathHtml = 'ruta/del/reporte/ExtentHtml.html'
    //                 def attachFilePathPdf = 'ruta/del/reporte/ExtentHtml.pdf'

    //                 // Verificación de archivos usando comandos de shell
    //                 bat """
    //                     if [ ! -f ${attachFilePathHtml} ]; then
    //                         echo "Archivo HTML no encontrado en ${attachFilePathHtml}"
    //                         exit 1
    //                     fi
    //                     if [ ! -f ${attachFilePathPdf} ]; then
    //                         echo "Archivo PDF no encontrado en ${attachFilePathPdf}"
    //                         exit 1
    //                     fi
    //                 """

    //                 // Adjuntar los archivos en Jira
    //                 try {
    //                     def attachHtmlResponse = jiraAddAttachment site: 'PruebaEmpresa', issueKey: issueKey, file: attachFilePathHtml
    //                     echo "Archivo HTML adjuntado: ${attachHtmlResponse}"

    //                     def attachPdfResponse = jiraAddAttachment site: 'PruebaEmpresa', issueKey: issueKey, file: attachFilePathPdf
    //                     echo "Archivo PDF adjuntado: ${attachPdfResponse}"

    //                 } catch (Exception e) {
    //                     error "Error adjuntando archivos: ${e.message}"
    //                 }
    //             }
    //         }
    //     }

    stage('Crear caso en Jira') {
    steps {
        script {
            // Datos para crear el issue en Jira
            def jiraIssueInput = [
                fields: [
                    project: [ key: 'TESTEAME' ],
                    summary: 'Resultados de pruebas automatizadas',
                    description: 'Las pruebas automatizadas se ejecutaron correctamente. Ver adjuntos para más detalles.',
                    issuetype: [ name: 'Bug' ]
                ]
            ]

            // Creación del issue en Jira
            def response = jiraNewIssue issue: jiraIssueInput, site: 'PruebaEmpresa'
            echo "Jira issue response: ${response}"

            // Verificación del issueKey
            def issueKey = response?.data?.key
            if (!issueKey) {
                error "No se pudo obtener el issueKey de la respuesta de Jira"
            } else {
                echo "IssueKey obtenido: ${issueKey}"
            }

            // Rutas de los archivos de reportes
            // def attachFilePathHtml = 'ruta/del/reporte/ExtentHtml.html'
            // def attachFilePathPdf = 'ruta/del/reporte/ExtentHtml.pdf'
            def attachFilePathHtml = "ExtentReports/SparkReport_ 1_oct_24 15_00_13/HtmlReport/ExtentHtml.html"
            def attachFilePathPdf = "ExtentReports/SparkReport_ 1_oct_24 15_00_13/PdfReport/ExtentPdf.pdf"
            // def attachFilePathHtml = String.format('ExtentReports/SparkReport_ %s/HtmlReport/ExtentHtml.html', '1_oct_24 15_00_13')
            // def attachFilePathPdf = String.format('ExtentReports/SparkReport_ %s/PdfReport/ExtentPdf.pdf', '1_oct_24 15_00_13')

            

            // Verificación de archivos usando comandos de shell de Windows
            bat """
                IF NOT EXIST ${attachFilePathHtml} (
                    echo "Archivo HTML no encontrado en ${attachFilePathHtml}"
                    exit /B 1
                )
                IF NOT EXIST ${attachFilePathPdf} (
                    echo "Archivo PDF no encontrado en ${attachFilePathPdf}"
                    exit /B 1
                )
            """

            // Adjuntar los archivos en Jira
            try {
                def attachHtmlResponse = jiraAddAttachment site: 'PruebaEmpresa', issueKey: issueKey, file: attachFilePathHtml
                echo "Archivo HTML adjuntado: ${attachHtmlResponse}"

                def attachPdfResponse = jiraAddAttachment site: 'PruebaEmpresa', issueKey: issueKey, file: attachFilePathPdf
                echo "Archivo PDF adjuntado: ${attachPdfResponse}"

            } catch (Exception e) {
                error "Error adjuntando archivos: ${e.message}"
            }
        }
    }
}

// stage('Crear caso en Jira') {
//     steps {
//         script {
//             // Datos para crear el issue en Jira
//             def jiraIssueInput = [
//                 fields: [
//                     project: [ key: 'TESTEAME' ],
//                     summary: 'Resultados de pruebas automatizadas',
//                     description: 'Las pruebas automatizadas se ejecutaron correctamente. Ver adjuntos para más detalles.',
//                     issuetype: [ name: 'Bug' ]
//                 ]
//             ]

//             // Creación del issue en Jira
//             def response = jiraNewIssue issue: jiraIssueInput, site: 'PruebaEmpresa'
//             echo "Jira issue response: ${response}"

//             // Verificación del issueKey
//             def issueKey = response?.data?.key
//             if (!issueKey) {
//                 error "No se pudo obtener el issueKey de la respuesta de Jira"
//             } else {
//                 echo "IssueKey obtenido: ${issueKey}"
//             }

//             // Listar archivos en el directorio de reportes
//             // bat 'dir ExtentReports/SparkReport_*'

//             // Verificación de archivos usando comandos de shell de Windows
//             bat """
//                 for /f "delims=" %%F in ('dir /b ExtentReports/SparkReport_*/HtmlReport/ExtentHtml.html') do (
//                     set "attachFilePathHtml=%%F"
//                     goto :htmlfound
//                 )
//                 echo "Archivo HTML no encontrado"
//                 exit /B 1
//                 :htmlfound
                
//                 for /f "delims=" %%F in ('dir /b ExtentReports/SparkReport_*/PdfReport/ExtentPdf.pdf') do (
//                     set "attachFilePathPdf=%%F"
//                     goto :pdffound
//                 )
//                 echo "Archivo PDF no encontrado"
//                 exit /B 1
//                 :pdffound

//                 echo "Archivo HTML encontrado: !attachFilePathHtml!"
//                 echo "Archivo PDF encontrado: !attachFilePathPdf!"
//             """

//             // Adjuntar los archivos en Jira
//             try {
//                 def attachHtmlResponse = jiraAddAttachment site: 'PruebaEmpresa', issueKey: issueKey, file: attachFilePathHtml
//                 echo "Archivo HTML adjuntado: ${attachHtmlResponse}"

//                 def attachPdfResponse = jiraAddAttachment site: 'PruebaEmpresa', issueKey: issueKey, file: attachFilePathPdf
//                 echo "Archivo PDF adjuntado: ${attachPdfResponse}"

//             } catch (Exception e) {
//                 error "Error adjuntando archivos: ${e.message}"
//             }
//         }
//     }
// }


        stage('Actualizar estado de Jira') {
            steps {
                script {
                    def testResult = currentBuild.result ?: 'SUCCESS'
                    
                    // Actualizar el estado en Jira dependiendo del resultado
                    if (testResult == 'SUCCESS') {
                        jiraTransitionIssue idOrKey: jiraIssueKey, transitionName: 'Done'
                    } else {
                        jiraTransitionIssue idOrKey: jiraIssueKey, transitionName: 'In Progress'
                    }
                }
            }
        }
    }
    
    post {
        always {
            // Limpiar workspace
            //cleanWs()
            echo 'No se limpia el workspace.'
        }
    }
}