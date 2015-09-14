package pl.touk.scalajs

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html.Div
import org.scalajs.dom.ext._

import scala.concurrent.Future
import scala.scalajs.js.annotation.JSExport
import scala.util.Failure
import scalatags.JsDom.all._


import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import upickle.default._

@JSExport
class HelloWorld {

  private def getData(data: String): Future[List[GeoLocationWithComments]] =
    readAjax[List[GeoLocation]](s"http://nominatim.openstreetmap.org/search?q=$data&format=json")
      .flatMap(list => Future.sequence(list.map(loc => getComments(loc.name).map(GeoLocationWithComments(loc, _)))))

  private def getComments(oneLocation: String) =
    readAjax[List[Comment]](s"http://localhost:8080/locationComment/$oneLocation")

  private def readAjax[T: Reader](url: String) = Ajax.get(url)
    .map(_.responseText).map(read[T]).andThen {
    case Failure(ex) => dom.window.alert(s"Failed with ${ex.getMessage}, $ex")
  }

  @JSExport
  def run(): Unit = {
    val inputBox = input(`type` := "text").render
    val listDiv = div().render
    document.body.appendChild(div(inputBox, button("Load", onclick := { () =>
      getData(inputBox.value).foreach(fill(listDiv))
    }), listDiv).render)
  }

  private def fill(divel: Div)(locations: List[GeoLocationWithComments]) = {
    divel.children.foreach(divel.removeChild)
    divel.appendChild(ul(locations.map(loc => li(s"${loc.location.name}: ${loc.comments}"))).render)

  }

}
