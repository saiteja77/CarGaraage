pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        withGradle() {
          sh 'clean test'
        }

      }
    }

  }
}