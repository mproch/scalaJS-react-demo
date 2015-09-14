enablePlugins(ScalaJSPlugin)

name := "scalaJDay"

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "com.lihaoyi" %%% "scalatags" % "0.5.2"

)

