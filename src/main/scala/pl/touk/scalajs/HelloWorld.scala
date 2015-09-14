package pl.touk.scalajs

import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom
import dom.document

import scalatags.JsDom.tags._
import scalatags.JsDom.attrs._
import scalatags.JsDom.short._


@JSExport
class HelloWorld {

  @JSExport
  def run(): Unit = {

    println("hello world")
    val list = ul().render

    val innerContent = div(id := "content3", onclick := { () => {
        println("hello?")
        list.appendChild(li("JDay is gr8!").render)
      }},
      "hello world",
      list
    )

    document.getElementById("content").appendChild(innerContent.render)

  }



}
