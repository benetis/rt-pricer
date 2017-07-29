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


case class RTItem(
                        itemId: Option[String],
                        url: Option[String],
                        price: Option[Double],
                        pricePerMeter: Option[Double],
                        area: Option[Double],
                        rooms: Option[Int],
                        floor: Option[Int],
                        numberOfFloors: Option[Int],
                        buildYear: Option[Int],
                        houseType: Option[String],
                        heatingSystem: Option[String],
                        equipment: Option[String],
                        shortDescription: Option[String],
                        comment: Option[String],
                        created: Option[String],
                        edited: Option[String],
                        interested: Option[String]
                      )

case class RTDetailsArea(value: Double) {
}

case class RTDetailsNumberOfRooms(value: Int)

case class RTDetailsFloor(value: Int)

case class RTDetailsNumberOfFloors(value: Int)

case class RTDetailsBuildYear(value: Int)

case class RTDetailsHouseType(value: String)

case class RTDetailsHeatingSystem(value: String)

case class RTDetailsEquipment(value: String)

case class RTDetailsShortDescription(value: String)


class Classifier {

}
