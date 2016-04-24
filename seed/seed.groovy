mavenJob("01_UnitTests"){
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
    goals "clean package"

    publishers {
        archiveArtifacts {
            pattern('**/*.jar')
            onlyIfSuccessful()
        }
    }
}