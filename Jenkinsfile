pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Testing...'
        sh 'gradle clean test'
      }
    }

  }
}