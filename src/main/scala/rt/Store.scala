package rt

import com.paulgoldbaum.influxdbclient.Parameter.Precision
import com.paulgoldbaum.influxdbclient._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class Store {
  val influxdb: InfluxDB = InfluxDB.connect("localhost", 8086)
  val database: Database = influxdb.selectDatabase("rt-prices")

  def writeList(list: Seq[Option[String]]): Future[Boolean] = {
    val points = list
      .map { item =>
        Point("Item", System.nanoTime())
          .addField("url", item.getOrElse("failed-to-parse"))
      }

    database.bulkWrite(points, precision = Precision.NANOSECONDS)
  }
}
