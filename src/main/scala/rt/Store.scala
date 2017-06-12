package rt

import com.paulgoldbaum.influxdbclient.Parameter.Precision
import com.paulgoldbaum.influxdbclient._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class Store {
  val influxdb: InfluxDB = InfluxDB.connect("localhost", 8086)
  val database: Database = influxdb.selectDatabase("rt-prices")

  def writeDetails(details: RTDetails,
                   rtSite: RTSite,
                   rtCategory: RTCategory): Future[Boolean] = {


    val point = Point("RTItem", System.nanoTime())
      .addTag("id", details.id.getOrElse(""))
      .addTag("category", rtSite.categoryId(rtCategory))
      .addField("url", details.url.getOrElse(""))
      .addTag("price", details.price.getOrElse(""))
      .addTag("area", details.area.getOrElse(0f).toString)
      .addTag("rooms", details.rooms.getOrElse(0).toString)
      .addField("floor", details.floor.getOrElse(-1))
      .addField("house-type", details.houseType.getOrElse(""))
      .addField("heating-system", details.heatingSystem.getOrElse(""))
      .addField("equipment", details.equipment.getOrElse(""))
      .addField("short-description", details.shortDescription.getOrElse(""))
      .addField("comment", details.comment.getOrElse(""))

    database.write(point, precision = Precision.NANOSECONDS)
  }

}
