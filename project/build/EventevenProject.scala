import sbt._

class EvenstevenProject(info: ProjectInfo) extends DefaultWebProject(info) {
    val liftVersion = "2.3"
    // Dependencies

    override def libraryDependencies = Set(
      "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
      "net.liftweb" %% "lift-mapper" % liftVersion % "compile",
      "org.mortbay.jetty" % "jetty" % "6.1.22" % "test",
      "org.scalatest" % "scalatest" % "1.2",
      "ch.qos.logback" % "logback-classic" % "0.9.26",
      "com.h2database" % "h2" % "1.2.138",
      "junit" % "junit" % "4.5" % "test",
      "org.scala-tools.testing" %% "specs" % "1.6.6" % "test"
) ++ super.libraryDependencies
}
