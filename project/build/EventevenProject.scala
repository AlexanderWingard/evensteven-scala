import sbt._

class EvenstevenProject(info: ProjectInfo) extends DefaultProject(info) {
    // Dependencies
    val scalatest = "org.scalatest" % "scalatest" % "1.2"
}
