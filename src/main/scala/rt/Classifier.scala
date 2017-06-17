package rt

trait RTCategory {
}

trait RTSite {
  def categoryId(category: RTCategory): String
}

case class RTFlatsRent() extends RTCategory

case class RTFlatsSell() extends RTCategory

case class RTHousesSell() extends RTCategory

case class RTAruodas() extends RTSite {

  override def categoryId(category: RTCategory): String = category match {
    case RTFlatsRent() => "4"
    case RTFlatsSell() => "1"
    case RTHousesSell() => "2"
  }

}

object RTDetailsConversions {
  implicit def convertGeneric(rTDetails: Option[RTDetails]): String = {
    rTDetails.flatMap(_.value).getOrElse("None").toString
  }

}

trait RTDetails {
  val value: Option[Any]
}

/**
  * Details are a part above the ad, where DD / DT is used show categorized info
  * Item is one RT ad
  */

case class RTDetailsArea(value: Option[Double]) extends RTDetails {
}
case class RTDetailsPrice(value: Option[Double]) extends RTDetails
case class RTDetailsPricePerMeter(value: Option[Double]) extends RTDetails
case class RTDetailsNumberOfRooms(value: Option[Int]) extends RTDetails
case class RTDetailsFloor(value: Option[Int]) extends RTDetails
case class RTDetailsNumberOfFloors(value: Option[Int]) extends RTDetails
case class RTDetailsBuildYear(value: Option[Int]) extends RTDetails
case class RTDetailsHouseType(value: Option[String]) extends RTDetails
case class RTDetailsHeatingSystem(value: Option[String]) extends RTDetails
case class RTDetailsEquipment(value: Option[String]) extends RTDetails
case class RTDetailsShortDescription(value: Option[String]) extends RTDetails

case class RTItem(id: Option[String],
                  url: Option[String],
                  price: Option[RTDetailsPrice],
                  pricePerMeter: Option[RTDetailsPricePerMeter],
                  area: Option[RTDetailsArea],
                  rooms: Option[RTDetailsNumberOfRooms],
                  floor: Option[RTDetailsFloor],
                  numberOfFloors: Option[RTDetailsNumberOfFloors],
                  buildYear: Option[RTDetailsBuildYear],
                  houseType: Option[RTDetailsHouseType],
                  heatingSystem: Option[RTDetailsHeatingSystem],
                  equipment: Option[RTDetailsEquipment],
                  shortDescription: Option[RTDetailsShortDescription],
                  comment: Option[String],
                  created: Option[String],
                  edited: Option[String],
                  interested: Option[String]
                    )


class Classifier {

}
