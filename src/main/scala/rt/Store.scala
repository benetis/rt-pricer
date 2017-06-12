package rt

import com.paulgoldbaum.influxdbclient.Parameter.Precision
import com.paulgoldbaum.influxdbclient._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class Store {
  val influxdb: InfluxDB = InfluxDB.connect("localhost", 8086)
  val database: Database = influxdb.selectDatabase("rt-prices")

  def writeList(list: Seq[Option[String]],
                rTSite: RTSite,
                rTCategory: RTCategory): Future[Boolean] = {
    val points = list
      .map { item => {

        val (url, id) = item match {
          case Some(url: String) => (url, url.substring(url.length - 10, url.length - 1))
          case None => ("", "url-not-found")
        }

        Point("Item", System.nanoTime())
          .addTag("id", id)
          .addTag("url", url)

      }

      }

    database.bulkWrite(points, precision = Precision.NANOSECONDS)
  }

  def getList(): Future[List[Record]] = {
    val result = database.query("SELECT distinct(\"url\") as \"url\" FROM \"rt-prices\".\"autogen\".\"Item\"")
    result.map { res => res.series.head.records }
  }
}
