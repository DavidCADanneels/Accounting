mavenJob("01_UnitTests"){
    triggers {
        scm 'H/15 * * * *'
    }
    scm{
        github("https://github.com/DavidCADanneels/Accounting.git","master")
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