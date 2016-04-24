mavenJob("01_UnitTests"){
    triggers {
        scm 'H/15 * * * *'
    }
    scm{
        github("DavidCADanneels/Accounting","master") {node ->
            node / 'locations' / 'hudson.scm.SubversionSCM_-ModuleLocation' / 'credentialsId' << 'GitHub'
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