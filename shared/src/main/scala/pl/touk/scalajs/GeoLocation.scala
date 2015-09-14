package pl.touk.scalajs

import derive.key

case class GeoLocation(@key("display_name") name: String,
                       @key("lat") latitude: Double, @key("lon") longitude: Double)


case class GeoLocationWithComments(location: GeoLocation, comments:List[Comment])


