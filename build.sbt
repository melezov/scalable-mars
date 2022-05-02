organization := "learning-is-happening"
name := "scalable-mars"

ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "3.1.2"

val core = (project
  settings(
    scalaVersion := "3.1.2",
    libraryDependencies ++= Seq(
      "dev.zio"            %% "zio"           % "2.0.0-RC5",
      "org.apache.commons" %  "commons-math3" % "3.6.1",

      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.2" % Test,
    )
  )
)
