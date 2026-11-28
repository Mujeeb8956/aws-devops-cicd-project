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

        stage('ECR Login') {
            steps {
                sh '''
                    aws ecr get-login-password \
                      --region ap-southeast-2 | \
                    docker login \
                      --username AWS \
                      --password-stdin \
                      362457597883.dkr.ecr.ap-southeast-2.amazonaws.com
                '''
            }
        }

        stage('Docker Push to ECR') {
            steps {
                sh '''
                    docker tag \
                      aws-devops-cicd-project:${BUILD_NUMBER} \
                      362457597883.dkr.ecr.ap-southeast-2.amazonaws.com/aws-devops-cicd-project:${BUILD_NUMBER}

                    docker push \
                      362457597883.dkr.ecr.ap-southeast-2.amazonaws.com/aws-devops-cicd-project:${BUILD_NUMBER}
                '''
            }
        }

        stage('Automatic Docker Deployment') {
            steps {
                sh '''
                    set -eu

                    IMAGE_URI="362457597883.dkr.ecr.ap-southeast-2.amazonaws.com/aws-devops-cicd-project:${BUILD_NUMBER}"
                    CONTAINER_NAME="aws-devops-app"

                    echo "Saving previous image for rollback..."

                    PREVIOUS_IMAGE_ID=$(docker inspect \
                      --format='{{.Image}}' \
                      "$CONTAINER_NAME" 2>/dev/null || true)

                    echo "Pulling new image from ECR..."

                    docker pull "$IMAGE_URI"

                    echo "Removing previous container..."

                    docker rm -f "$CONTAINER_NAME" || true

                    echo "Starting new application container..."

                    if ! docker run -d \
                      --restart unless-stopped \
                      --name "$CONTAINER_NAME" \
                      -p 8081:8081 \
                      "$IMAGE_URI"; then

                        echo "New deployment failed. Attempting rollback..."

                        if [ -n "$PREVIOUS_IMAGE_ID" ]; then
                            docker run -d \
                              --restart unless-stopped \
                              --name "$CONTAINER_NAME" \
                              -p 8081:8081 \
                              "$PREVIOUS_IMAGE_ID"
                        fi

                        exit 1
                    fi

                    echo "Waiting for application health check..."

                    for i in $(seq 1 30); do

                        if curl -fsS \
                          http://localhost:8081/api/health; then

                            echo "Application deployed successfully!"
                            exit 0
                        fi

                        echo "Application not ready yet. Attempt $i/30"
                        sleep 5
                    done

                    echo "Health check failed!"

                    docker logs "$CONTAINER_NAME" || true

                    docker rm -f "$CONTAINER_NAME" || true

                    echo "Attempting rollback..."

                    if [ -n "$PREVIOUS_IMAGE_ID" ]; then
                        docker run -d \
                          --restart unless-stopped \
                          --name "$CONTAINER_NAME" \
                          -p 8081:8081 \
                          "$PREVIOUS_IMAGE_ID"
                    fi

                    exit 1
                '''
            }
        }
    }
}

