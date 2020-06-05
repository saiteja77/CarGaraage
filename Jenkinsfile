pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Testing...'
        sh '''gradle clean test
gradle bootJar
docker build -t bitbyte01/cargaraage:latest .'''
      }
    }

    stage('publish') {
      steps {
        sh 'ls -l'
      }
    }

  }
}