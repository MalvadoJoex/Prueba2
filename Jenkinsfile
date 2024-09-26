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

        stage('Run Test'){
            steps {
                sh 'gradle test -Dcucumber.options="--tags @SaucedemoTest"'
            }
        }
    }
}