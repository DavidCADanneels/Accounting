mavenJob("01_UnitTests"){
    triggers {
        scm 'H/15 * * * *'
    }
    scm{
        git{
            remote {
                url "https://github.com/DavidCADanneels/Accounting.git"
            }
            branch "master"
            createTag false
        }
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