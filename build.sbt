organization := "learning-is-happening"
name := "scalable-mars"

ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.0"

val core = (project
  settings(
    libraryDependencies ++= Seq(
      "dev.zio"            %% "zio"           % "2.0.15",
      "org.apache.commons" %  "commons-math3" % "3.6.1",

      "dev.zio" %% "zio-test"          % "2.0.15" % Test,
      "dev.zio" %% "zio-test-sbt"      % "2.0.15" % Test,
      "dev.zio" %% "zio-test-magnolia" % "2.0.15" % Test,
    ),

    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )
)
