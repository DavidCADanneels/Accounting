job("${SEED_PROJECT}-${SEED_BRANCH}-build"){
    triggers {
        scm 'H/15 * * * *'
    }
    scm{
        git {
            remote {
                github 'DavidCADanneels/Accounting', 'https'
                credentials 'GitHub'
            }
            branch "master"
            createTag false
        }
    }
    steps{
        maven{
            goals """\
org.codehaus.mojo:versions-maven-plugin:2.2:set
-DgenerateBackupPoms=false
-DnewVersion=${SEED_BRANCH}-\${BUILD_NUMBER}
-DartifactId=*
-DgroupId=*
-DoldVersion=*
"""
        }
        maven{
            goals "clean package"
        }
    }

    publishers {
        archiveArtifacts {
            pattern('**/*.jar')
            onlyIfSuccessful()
        }
        archiveJunit '**/surefire-reports/*.xml'
        jacocoCodeCoverage {
            execPattern '**/***.exec'
            classPattern '**/classes'
            sourcePattern '**/src/main/java'
            inclusionPattern '**/*.class'
            exclusionPattern '**/*Test*'
        }
    }
}