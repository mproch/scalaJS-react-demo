import com.lihaoyi.workbench.Plugin._
import spray.revolver.RevolverPlugin.Revolver

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
    Revolver.settings.settings ++
      (libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream-experimental" % "1.0",
      "com.typesafe.akka" %% "akka-http-core-experimental" % "1.0",
      "com.typesafe.akka" %% "akka-http-experimental" % "1.0"
    )) : _*
  ).
  jsSettings(
    workbenchSettings ++ Seq(
      bootSnippet := "document.getElementById(\"content\").innerHTML = \"\"; new pl.touk.scalajs.HelloWorld().run();",
      updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile),
      scalaVersion := "2.11.5",
      libraryDependencies ++= Seq(
        "com.github.japgolly.scalajs-react" %%% "core" % "0.9.2",
        "com.github.chandu0101.scalajs-react-components" %%% "core" % "0.2.0-SNAPSHOT"),
      jsDependencies +=
      "org.webjars" % "react" % "0.12.2" / "react-with-addons.js" commonJSName "React"
    ) : _*
  )


lazy val scalaJDayJS = scalaJDay.js
lazy val scalaJDayJVM = scalaJDay.jvm

