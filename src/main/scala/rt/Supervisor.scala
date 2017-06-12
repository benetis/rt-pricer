package rt

import akka.actor.{Actor, ActorRef, ActorSystem, _}

case class StartDetails(rtSite: RTSite,
                        rTCategory: RTCategory)

case class StoreDetails(details: RTDetails,
                        rtSite: RTSite,
                        rTCategory: RTCategory)

case class EndDetails()

case class ScrapDetails(url: String,
                        rtSite: RTSite,
                        rTCategory: RTCategory)

case class RTDetails(id: Option[String],
                     url: Option[String],
                     price: Option[String],
                     area: Option[Double],
                     rooms: Option[Int],
                     floor: Option[Int],
                     houseType: Option[String],
                     heatingSystem: Option[String],
                     equipment: Option[String],
                     shortDescription: Option[String],
                     comment: Option[String],
                     created: Option[String],
                     edited: Option[String],
                     interested: Option[String]
                    )

class Supervisor(system: ActorSystem) extends Actor {

  var host2Actor = Map.empty[String, ActorRef]

  val store = new Store

  val scrapers = system.actorOf(Props(new Scraper(self)))

  override def receive: Receive = {
    case StartDetails(rtSite: RTSite, rTCategory: RTCategory) => startDetails(rtSite, rTCategory)
    case StoreDetails(details: RTDetails, rtSite: RTSite, rtCategory: RTCategory) =>
      storeDetails(details, rtSite, rtCategory)
    case EndDetails => ???
  }

  private def storeDetails(details: RTDetails,
                           rtSite: RTSite,
                           rtCategory: RTCategory) = {
    store.writeDetails(details, rtSite, rtCategory)
  }

  private def startDetails(site: RTSite,
                           category: RTCategory) = {

    scrapers ! ScrapDetails(
      "https://en.aruodas.lt/butai-vilniuje-fabijoniskese-fabijoniskiu-g-paskutinis-loftas-grande-skelbiamas-1-2280415/",
      site,
      category
    )

    //    store.getList().map(records => {
    //      records.foreach(r => {
    //        val url: String = r("url").toString
    //        scrapers ! ScrapDetails(url, rtS)
    //        Thread.sleep(2000) //throttle to not kill the site
    //      })
    //    })

  }


}
