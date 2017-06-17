package rt

import akka.actor.{Actor, ActorRef, ActorSystem, _}

case class StartDetails(rtSite: RTSite,
                        rTCategory: RTCategory)

case class StoreDetails(details: RTItem,
                        rtSite: RTSite,
                        rTCategory: RTCategory)

case class EndDetails()

case class ScrapDetails(url: String,
                        rtSite: RTSite,
                        rTCategory: RTCategory)

class Supervisor(system: ActorSystem) extends Actor {

  var host2Actor = Map.empty[String, ActorRef]

  val store = new Store

  val scrapers = system.actorOf(Props(new Scraper(self)))

  override def receive: Receive = {
    case StartDetails(rtSite: RTSite, rTCategory: RTCategory) => startDetails(rtSite, rTCategory)
    case StoreDetails(details: RTItem, rtSite: RTSite, rtCategory: RTCategory) =>
      storeDetails(details, rtSite, rtCategory)
    case EndDetails => ???
  }

  private def storeDetails(details: RTItem,
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
