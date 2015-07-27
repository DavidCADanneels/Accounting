mavenJob("01_UnitTests"){
    triggers {
        scm('H/15 * * * *')
    }
//    rootPOM("pom.xml")
    goals("clean package")
}