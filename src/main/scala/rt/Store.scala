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
      .addTag("id", details.id)
      .addTag("category", rtSite.categoryId(rtCategory))
      .addField("url", details.url)
      .addTag("price", details.price.toString)
      .addTag("area", details.area.toString)
      .addTag("rooms", details.rooms.toString)
      .addField("floor", details.floor)
      .addField("house-type", details.houseType)
      .addField("heating-system", details.heatingSystem)
      .addField("equipment", details.equipment)
      .addField("short-description", details.shortDescription)
      .addField("comment", details.comment)

    database.write(point, precision = Precision.NANOSECONDS)
  }

}
