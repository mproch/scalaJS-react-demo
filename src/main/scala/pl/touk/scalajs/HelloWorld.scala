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

@JSExport
class HelloWorld {

  private def getData(data:String): Future[List[String]] = {
    val url =s"http://nominatim.openstreetmap.org/search?q=$data&format=json"
    val result = Ajax.get(url).map(xhr => parseData(js.JSON.parse(xhr.responseText)))
    result.onFailure {
      case ex: Throwable => dom.window.alert(s"Failed with ${ex.getMessage}")
    }
    result
  }

  private def parseData(dynamic: js.Dynamic) = {
    dynamic.asInstanceOf[js.Array[js.Dynamic]].toList.map(p => s"Name: ${p.display_name}, lat: ${p.lat}, lon: ${p.lon}")
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
      case cont => cont.foreach(v => list.appendChild(li(v).render))
    }

    document.getElementById("content").appendChild(innerContent.render)

  }



}
