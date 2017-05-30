package rt

trait RTCategory {
}

case class RTFlatsRent() extends RTCategory
case class RTFlatsSell() extends RTCategory
case class RTHouseRent() extends RTCategory

case class RTAruodas() {
  val sellFlatsUrl = "https://www.aruodas.lt/butai/kaune/?FDistrict=6&obj=1&FOrder=Actuality&FRegion=43&mod=Siulo&act=makeSearch&Page=1"

  /** Next page of "high" view category **/
  def nextPage(category: RTCategory): String = category match {
    case _ => "https://www.aruodas.lt/butai/kaune/?FDistrict=6&obj=1&FOrder=Actuality&FRegion=43&mod=Siulo&act=makeSearch&Page=2"
  }

}


class Classifier {

}
