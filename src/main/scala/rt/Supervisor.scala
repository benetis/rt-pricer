package rt

import akka.actor.{Actor, ActorRef, ActorSystem, _}
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.concurrent.ExecutionContext.Implicits.global

case class StartList(rtSite: RTSite, rTCategory: RTCategory)

case class StoreList(list: Seq[Option[String]],
                     rTSite: RTSite,
                     rTCategory: RTCategory)

case class EndList()

case class StartDetails(rtSite: RTSite,
                        rTCategory: RTCategory)

case class StoreDetails(details: List[RTDetails])

case class EndDetails()

case class ScrapList(url: String, rTSite: RTSite, rTCategory: RTCategory)

case class ScrapDetails(url: String)

case class RTDetails()

class Supervisor(system: ActorSystem) extends Actor {

  var host2Actor = Map.empty[String, ActorRef]

  val store = new Store

  val scrapers = system.actorOf(Props(new Scraper(self)))

  override def receive: Receive = {
    case StartList(rtSite: RTSite, rtCategory: RTCategory) => startList(rtSite, rtCategory)
    case StoreList(list: Seq[Option[String]], rtSite: RTSite, rtCategory: RTCategory) =>
      storeList(list, rtSite, rtCategory)
    case EndList => ???
    case StartDetails(rtSite: RTSite, rTCategory: RTCategory) => startDetails(rtSite, rTCategory)
    case StoreDetails(details: List[RTDetails]) => ???
    case EndDetails => ???
  }

  private def startList(rtSite: RTSite, rtCategory: RTCategory) = {
    (1 to 1).foreach { page =>
      scrapers ! ScrapList(rtSite.nextPage(RTFlatsRent(), page), rtSite, rtCategory)
    }
  }

  private def storeList(list: Seq[Option[String]],
                        rtSite: RTSite,
                        rtCategory: RTCategory) = {
    val result = store.writeList(list, rtSite, rtCategory)

    list |> println
  }

  private def startDetails(site: RTSite,
                           category: RTCategory) = {

    store.getList().map(records => {
      records.foreach(r => {
        val url: String = r("url").toString
        scrapers ! ScrapDetails(url)
        Thread.sleep(2000) //throttle to not kill the site
      })
    })

  }


}
