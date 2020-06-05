pipeline {
  agent any
  environment {
        MONGO_URI     = credentials('MONGO_URI')
    }
  stages {
    stage('Build') {
      withCredentials([usernamePassword(credentialsId: 'MONGO_URI', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        // available as an env variable, but will be masked if you try to print it out any which way
        // note: single quotes prevent Groovy interpolation; expansion is by Bourne Shell, which is what you want
        sh 'echo $PASSWORD'
        // also available as a Groovy variable
        echo USERNAME
        // or inside double quotes for string interpolation
        echo "username is $USERNAME"
        }
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
