
pipeline {
    agent any

    tools {
        maven 'Maven-3.9.12'
    }

    stages {

        stage('Git Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Mujeeb8956/aws-devops-cicd-project.git'
            }
        }

        stage('Maven Build and Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.2.0.4988:sonar
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build \
                      -t aws-devops-cicd-project:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Trivy Security Scan') {
            steps {
                sh '''
                    TMPDIR=/var/tmp/trivy-tmp trivy \
                      --cache-dir /var/cache/trivy \
                      image \
                      --severity HIGH,CRITICAL \
                      --exit-code 1 \
                      aws-devops-cicd-project:${BUILD_NUMBER}
                '''
            }
        }
    }
}

