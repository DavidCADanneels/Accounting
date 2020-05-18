def mavenInstallation = 'Maven 3.3.3'
def jdkVersion = 'JDK8'
def baseVersion

pipeline {
    agent any
    stages {
        stage('Define Version') {
            steps{
                script {
                    echo "running on ${BRANCH_NAME}"
                    baseVersion = "${BRANCH_NAME}"
                    baseVersion = baseVersion.replaceAll('release/','')
                    baseVersion = baseVersion.replaceAll('/','-')
                    int nr = 0
                    List<String> tags = sh(returnStdout: true, script: "git tag --list --sort=-version:refname ${branch}.*").split()
                    if (tags.isEmpty()) {
                        echo "No previous tag with pattern ${baseVersion}.* was found, returning ${baseVersion}.0"
                    } else {
                        def latestTag = tags.get(0)
                        int lastIndex = latestTag.lastIndexOf('.')
                        echo ""
                        nr = latestTag.substring(latestTag+1) as Integer
                        nr++
                        echo ""
                    }
                    version = "${baseVersion}.${nr}"
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