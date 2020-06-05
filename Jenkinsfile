pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        withCredentials(bindings: [usernamePassword(credentialsId: 'MONGO_URI', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh 'echo $PASSWORD'
          echo USERNAME
          sh 'echo $USERNAME'
        }

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
  environment {
    MONGO_URI = credentials('MONGO_URI')
  }
}