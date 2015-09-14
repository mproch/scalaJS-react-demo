package pl.touk.scalajs

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.attrs._
import scalatags.JsDom.short._
import scalatags.JsDom.tags._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import upickle.default._

@JSExport
class HelloWorld {

  private def getData(data:String): Future[List[GeoLocation]] = {
    val url =s"http://nominatim.openstreetmap.org/search?q=$data&format=json"
    val result = Ajax.get(url).map(xhr => read[List[GeoLocation]](xhr.responseText))
    result.onFailure {
      case ex: Throwable => dom.window.alert(s"Failed with ${ex.getMessage}, $ex")
    }
    result
  }

  @JSExport
  def run(): Unit = {

    val list = ul().render

    val innerContent = div(id := "content3", onclick := { () => {
        println("hello?")
        list.appendChild(li("JDay is gr8!").render)
      }},
      "hello world",
      list
    )

    getData("Lviv").onSuccess {
      case cont => cont.foreach(v => list.appendChild(li(s"${v.name}, ${v.latitude}, ${v.longitude}").render))
    }

    document.getElementById("content").appendChild(innerContent.render)

  }



}
