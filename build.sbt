organization := "learning-is-happening"
name := "scalable-mars"
version := "0.0.1-SNAPSHOT"

scalaVersion := "3.1.2"
val zioVersion = "1.0.14" // latest: "2.0.0-RC5"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio"          % zioVersion,

  "dev.zio" %% "zio-test"     % zioVersion % Test,
  "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
