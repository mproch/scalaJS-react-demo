import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

name := "scalaJDay"

scalaVersion := "2.11.7"

lazy val root = project.in(file(".")).
  aggregate(scalaJDayJS, scalaJDayJVM).
  settings(
    publish := {},
    publishLocal := {}
  )

lazy val scalaJDay = crossProject.in(file(".")).
  settings(
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.11.5",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "upickle" % "0.3.6",
      "com.lihaoyi" %%% "scalatags" % "0.5.2"
    )
  ).
  jvmSettings(
  ).
  jsSettings(
    workbenchSettings ++ Seq(
      bootSnippet := "document.getElementById(\"content\").innerHTML = \"\"; new pl.touk.scalajs.HelloWorld().run();",
      updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile),
      scalaVersion := "2.11.5") : _*
  )


lazy val scalaJDayJS = scalaJDay.js
lazy val scalaJDayJVM = scalaJDay.jvm

