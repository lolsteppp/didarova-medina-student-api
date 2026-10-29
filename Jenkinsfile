// =====================================================================
// Jenkinsfile - didarova-medina-student-api (Declarative Pipeline)
// Jenkins job: Didarova_Medina_Student_API
// Student: Didarova Medina | ID 37944 | Group IT2-2313
// =====================================================================
pipeline {
    agent any                       // Docker-capable agent (docker CLI + access to Docker daemon)

    environment {
        STUDENT_NAME  = 'Didarova Medina'
        STUDENT_ID    = '37944'
        STUDENT_GROUP = 'IT2-2313'
        IMAGE_NAME    = 'didarova-medina-student-api'
        IMAGE_TAG     = 'v1.0-didarova-medina-37944'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '15'))
        timeout(time: 30, unit: 'MINUTES')
    }

    // Automation (Task 4.4): poll GitHub every ~2 minutes.
    // If Jenkins is reachable from the internet, a GitHub webhook can be used instead
    // (then enable "GitHub hook trigger for GITScm polling" in the job).
    triggers {
        pollSCM('H/2 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                echo "=== Pipeline of ${STUDENT_NAME} (ID ${STUDENT_ID}, group ${STUDENT_GROUP}) ==="
                checkout scm
                sh 'git log --oneline -5'
                echo "Checked out commit ${env.GIT_COMMIT} from branch ${env.GIT_BRANCH}"
            }
        }

        stage('Build') {
            steps {
                echo "Building Spring Boot application of ${STUDENT_NAME} with Gradle"
                sh 'chmod +x gradlew'
                sh './gradlew --no-daemon clean bootJar -x test'
                sh 'ls -lh build/libs/'
            }
        }

        stage('Test') {
            steps {
                echo "Running automated tests for ${STUDENT_NAME}"
                // if any test fails, gradlew exits with non-zero code -> stage FAILS -> pipeline STOPS
                sh './gradlew --no-daemon test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker image ${IMAGE_NAME}:${IMAGE_TAG} for ${STUDENT_NAME}"
                sh "docker build --build-arg APP_VERSION=${IMAGE_TAG} -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Verify') {
            steps {
                sh """
                    if docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} > /dev/null 2>&1; then
                        echo "VERIFY OK: image ${IMAGE_NAME}:${IMAGE_TAG} exists (owner: ${STUDENT_NAME})"
                        docker images ${IMAGE_NAME}
                        docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} --format 'Created: {{.Created}}  Size: {{.Size}} bytes'
                    else
                        echo "VERIFY FAILED: image ${IMAGE_NAME}:${IMAGE_TAG} not found"
                        exit 1
                    fi
                """
            }
        }
    }

    post {
        success {
            echo "SUCCESS: pipeline of ${STUDENT_NAME} finished. Image: ${IMAGE_NAME}:${IMAGE_TAG}"
        }
        failure {
            echo "FAILURE: pipeline of ${STUDENT_NAME} failed. Check the stage marked in red and the console output."
        }
    }
}
