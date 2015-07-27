mavenJob("01_UnitTests"){
    triggers {
        scm('H/15 * * * *')
    }
    scm{
        git("https://github.com/DavidCADanneels/Accounting.git")
    }
//    rootPOM("pom.xml")
    goals("clean package")
}