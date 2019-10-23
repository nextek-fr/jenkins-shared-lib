pipeline {

    agent {
        kubernetes {
            label 'jsl'
            defaultContainer 'jnlp'
            yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    pipeline: jenkinsfile
spec:
  containers:
    - name: 'maven'
      image: maven:3.3.9
      command:
        - cat
      tty: true
      volumeMounts:
        - mountPath: '/root/.m2'
          name: maven-repo
          readOnly: false
      resources:
       requests:
        cpu: "0.05"
        memory: "256Mi"
       limits:
        cpu: "2"
        memory: "2Gi"
  volumes:
    - name: maven-repo
      persistentVolumeClaim:
        claimName: maven-repo-pvc
"""
        }
    }

    options {
        skipDefaultCheckout(true)
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    deleteDir()
                    checkout scm
                }
            }
        }

        stage('Compile') {
            steps {
                container('maven') {
                    script {
                        sh """
                            mvn clean compile
                        """
                    }
                }
            }
        }

        stage('Test') {
            steps {
                container('maven') {
                    sh "mvn verify"
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
	}
}
