package pl.touk.scalajs

import chandu0101.scalajs.react.components.fascades.{Marker, LatLng}
import chandu0101.scalajs.react.components.maps.GoogleMap
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.ext._
import upickle.default._

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.annotation.JSExport
import scala.util.Failure

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
    React.render(LocationList(), document.body)
  }

  type State = (String, List[GeoLocationWithComments])

  case class ListBackend($: BackendScope[Unit, State]) {

    def valueChange(event: ReactEventI): Unit = {
      $.modState(s => (event.target.value, s._2))
    }

    def onSubmit(): Unit = {
      getData($.state._1).onSuccess { case locations => $.modState(s => (s._1, locations)) }
    }

  }

  val GeoLocation = ReactComponentB[GeoLocationWithComments]("GeoLocation")
    .render(locationWithComments => {
    val center = LatLng(locationWithComments.location.latitude, locationWithComments.location.longitude)
    <.div(<.b(locationWithComments.location.name),
        <.ul(
        locationWithComments.comments.map(comment => <.li(s"${comment.author} said ${comment.content}"))
      ))
  }).build

  val LocationList = ReactComponentB[Unit]("LocationList")
    .initialState(("", List[GeoLocationWithComments]()))
    .backend(ListBackend)
    .render(cScope => {
    val state = cScope.state
    val firstResult = state._2.headOption.map(toLatLng)
    <.div(
      <.input(^.`type` := "text", ^.value := state._1, ^.onChange ==> cScope.backend.valueChange),
      <.button("Search", ^.onClick --> cScope.backend.onSubmit),
      state._2.isEmpty ?= <.div(<.b("Sorry, no results")),
      <.ul(
        state._2.map(GeoLocation(_))
      ),
      firstResult.map(result => GoogleMap(center = result, markers = List(Marker(result)), zoom = 10))
    )
  })
    .buildU

  private def toLatLng(geoLocationWithComments: GeoLocationWithComments)
    = LatLng(geoLocationWithComments.location.latitude, geoLocationWithComments.location.longitude)

}
