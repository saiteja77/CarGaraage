pipeline {
  agent any
  environment {
        MONGO_URI     = credentials('MONGO_URI')
    }
  stages {
    stage('Build') {
      steps {
        echo 'Testing...'
        sh '''printenv
gradle clean test
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
