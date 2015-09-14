package pl.touk.scalajs

import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom
import dom.document

@JSExport
class HelloWorld {

  @JSExport
  def run(): Unit = {

    println("hello world")

    val inner = document.createElement("span")

    inner.appendChild(document.createTextNode("hello World"))

    document.body.appendChild(inner)
    document.getElementById("content").appendChild(inner)

  }



}
