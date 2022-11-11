import scala.scalanative.build.{GC, LTO, Mode}

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val sharedSettings = List(
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  Compile / nativeConfig ~= {
    _.withMode(Mode.releaseFull)
      .withLTO(LTO.full)
      .withGC(GC.default)
  },
  Test / nativeConfig ~= {
    _.withMode(Mode.debug)
      .withLTO(LTO.default)
      .withGC(GC.default)
  },
  nativeLinkingOptions += "-static"
)

lazy val `data-generator` = (project in file("modules/data-generator"))
  .enablePlugins(ScalaNativePlugin)
  .settings(sharedSettings)
  .settings(
    name := "data-generator",
    libraryDependencies ++= List(
      "org.scalacheck" %%% "scalacheck"      % "1.17.0",
      "org.typelevel"  %%% "cats-effect-std" % "3.3.14",
      "co.fs2"         %%% "fs2-io"          % "3.3.0",
      "com.monovore"   %%% "decline"         % "2.3.1",
      "io.circe"       %%% "circe-core"      % "0.14.3"
    )
  )

lazy val `tweet-generator` = (project in file("examples/tweet-generator"))
  .enablePlugins(ScalaNativePlugin)
  .dependsOn(`data-generator`)
  .settings(
    name := "tweet-generator",
    libraryDependencies ++= List(
      "eu.timepit" %%% "refined-scalacheck" % "0.10.1",
      "io.circe"   %%% "circe-generic"      % "0.14.3"
    )
  )
