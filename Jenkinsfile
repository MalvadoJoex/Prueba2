pipeline {
    agent any
    tools {
        gradle '8.2.1'
    }

    stages {
        stage('Mostrar PATH'){
            steps {
                sh 'echo $PATH'
            }
        }

        stage('Branches'){
            steps {
                //sh 'pip3 install --upgrade pip'
                sh 'java --version'

            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'gradle --version'
            }
        }
        stage("Build"){
            step{
                echo "Etapa BUILD no disponible"
            }
        }

        stage("Test"){
            step{
                echo "Etapa TEST no disponible"
            }
        }

        stage("Deploy"){
            steps{
                sh "docker-compose down -v"
                sh "docker-compose up -d --build"
            }
        }

        // stage('Run Test'){
        //     steps {
        //         sh 'gradle test -Dcucumber.options="--tags @SaucedemoTest"'
        //     }
        // }
    }
}