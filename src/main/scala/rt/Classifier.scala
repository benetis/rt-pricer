package rt

import com.netaporter.uri.dsl._

trait RTCategory {
}

trait RTSite {
  def nextPage(category: RTCategory,
               lastPage: Int): String

  def categoryId(category: RTCategory): String
}

case class RTFlatsRent() extends RTCategory

case class RTFlatsSell() extends RTCategory

case class RTHousesSell() extends RTCategory

case class RTAruodas() extends RTSite {
  val sellFlatsUri = "https://www.aruodas.lt/butai/kaune/?FDistrict=6&obj=1&FOrder=Actuality&FRegion=43&mod=Siulo&act=makeSearch" ? ("Page1" -> 1)

  /** Next page of "high" view category **/
  override def nextPage(category: RTCategory,
                        lastPage: Int): String = category match {
    case RTFlatsRent() => sellFlatsUri ? ("Page" -> lastPage)
  }

  override def categoryId(category: RTCategory): String = category match {
    case RTFlatsRent() => "4"
    case RTFlatsSell() => "1"
    case RTHousesSell() => "2"
  }

}


class Classifier {

}
