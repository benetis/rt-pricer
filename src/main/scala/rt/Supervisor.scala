package rt

import akka.actor.Status.Success
import akka.actor.{Actor, ActorRef, ActorSystem, _}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import scala.concurrent.ExecutionContext.Implicits.global

case class StartList(rtSite: RTSite)

case class StoreList(list: Seq[Option[String]])

case class EndList()

case class StartDetails(rtSite: RTSite,
                        rTCategory: RTCategory)

case class StoreDetails(details: List[RTDetails])

case class EndDetails()

case class ScrapList(url: String)

case class RTDetails()

class Supervisor(system: ActorSystem) extends Actor {

  var host2Actor = Map.empty[String, ActorRef]

  val store = new Store

  val scrapers = system.actorOf(Props(new Scraper(self)))

  override def receive: Receive = {
    case StartList(rtSite: RTSite) => startList(rtSite)
    case StoreList(list: Seq[Option[String]]) => storeList(list)
    case EndList => ???
    case StartDetails(rtSite: RTSite, rTCategory: RTCategory) => ???
    case StoreDetails(details: List[RTDetails]) => ???
    case EndDetails => ???
  }

  def startList(rtSite: RTSite) = {
    (1 to 1).foreach { page =>
      scrapers ! ScrapList(rtSite.nextPage(RTFlatsRent(), page))
    }
  }

  def storeList(list: Seq[Option[String]]) = {
    val result = store.writeList(list)

    list |> println
  }


}
