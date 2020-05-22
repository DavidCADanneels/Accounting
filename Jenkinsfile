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
                    List<String> tags = sh(returnStdout: true, script: "git tag --list --sort=-version:refname ${baseVersion}.*").split()
                    if (tags.isEmpty()) {
                        echo "No previous tag with pattern ${baseVersion}.* was found, returning ${baseVersion}.0"
                    } else {
                        def latestTag = tags.get(0)
                        int lastIndex = latestTag.lastIndexOf('.')
                        echo "Latest tag found with pattern ${baseVersion}.* was ${latestTag}"
                        nr = latestTag.substring(lastIndex+1) as Integer
                        nr++
                    }
                    version = "${baseVersion}.${nr}"
                    echo "version = ${version}"
                    currentBuild.description = version
                }
            }
        }
        stage('Set Version'){
            steps {
                withMaven(
                        maven: mavenInstallation,
                        jdk: jdkVersion
                ){
                    sh """\
mvn org.codehause.mojo:versions-maven-plugin:2.2:set \
-DgenerateBackupPoms=false \
-DprocessAllModules=true \
-DprocessDependencies=true \
-DnewVerson=${version} \
-DartifactId=* \
-DgroupId=* \
-DoldVersion=*
"""
                }
            }
        }
        stage('Build'){
            steps {
                withMaven(
                        maven: mavenInstallation,
                        jdk: jdkVersion
                ){
                    sh 'mvn clean package'

                }
            }
        }
    }
}