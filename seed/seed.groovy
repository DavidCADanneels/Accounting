job("Build_and_package"){
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
-DnewVersion=\${BRANCH}-\${BUILD_NUMBER}
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
        archiveJunit 'build/test-results/**/*.xml'
        jacocoCodeCoverage {
            execPattern '**/***.exec'
            classPattern '**/classes'
            sourcePattern '**/src/main/java'
            inclusionPattern '**/*.class'
            exclusionPattern '**/*Test*'
        }
    }
}