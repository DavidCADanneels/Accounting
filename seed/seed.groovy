mavenJob("01_UnitTests"){
    triggers {
        scm 'H/15 * * * *'
    }
    scm{
        github("DavidCADanneels/Accounting.git","master")
    }
//    rootPOM("pom.xml")
    goals "clean package"

    publishers {
        archiveArtifacts {
            pattern('**/*.jar')
            onlyIfSuccessful()
        }
    }
}