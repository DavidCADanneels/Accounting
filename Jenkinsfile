def mavenInstallation = 'Maven 3.3.3'
def jdkVersion = 'JDK8'

pipeline {
    agent any
    stages {
        stage('Define Version') {
            environment {
                POM_VERSION = "${readMavenPom().getVersion()}"
            }
            steps{
                script {
                    echo "running on ${BRANCH_NAME}"
                    def shortHash = sh(returnStdout: true, script: 'git  rev-parse --short HEAD')
                    shortHash = shortHash.trim()
                    version = POM_VERSION.replaceAll('SNAPSHOT', shortHash)
                    currentBuild.description = version
                }
            }
        }
        stage('Set Version'){
            steps {
                withMaven(
                        maven: mavenInstallation,
                        jdk: jdkVersion,
                        options: [
                                artifactsPublisher(disabled: true)
                        ]
                ){
                    sh 'mvn clean package'

                }
            }
        }
    }
}