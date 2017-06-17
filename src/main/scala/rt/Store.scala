package rt

import com.paulgoldbaum.influxdbclient.Parameter.Precision
import com.paulgoldbaum.influxdbclient._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import scala.language.implicitConversions
import rt.RTDetailsConversions._

class Store {
  val influxdb: InfluxDB = InfluxDB.connect("localhost", 8086)
  val database: Database = influxdb.selectDatabase("rt-prices")

  def writeDetails(item: RTItem,
                   rtSite: RTSite,
                   rtCategory: RTCategory): Future[Boolean] = {

    val point = Point("RTItem", System.nanoTime())
      .addTag("id", item.id.getOrElse(""))
      .addTag("category", rtSite.categoryId(rtCategory))
      .addField("url", item.url.getOrElse(""))
      .addTag("price", item.price)
      .addTag("area", item.area)
      .addTag("rooms", item.rooms)
      .addField("floor", item.floor)
      .addField("house-type", item.houseType)
      .addField("heating-system", item.heatingSystem)
      .addField("equipment", item.equipment)
      .addField("short-description", item.shortDescription)
      .addField("comment", item.comment.getOrElse(""))
      .addField("created", item.created.getOrElse(""))
      .addField("edited", item.edited.getOrElse(""))
      .addField("interested", item.interested.getOrElse(""))

    database.write(point, precision = Precision.NANOSECONDS)
  }

}
