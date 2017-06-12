package rt

import akka.actor.{Actor, ActorRef, ActorSystem, _}

case class StartDetails(rtSite: RTSite,
                        rTCategory: RTCategory)

case class StoreDetails(details: List[RTDetails],
                        rtSite: RTSite,
                        rTCategory: RTCategory)

case class EndDetails()

case class ScrapDetails(url: String,
                        rtSite: RTSite,
                        rTCategory: RTCategory)

case class RTDetails(id: String,
                     url: String,
                     price: Int,
                     area: Float,
                     rooms: Int,
                     floor: Int,
                     houseType: String,
                     heatingSystem: String,
                     equipment: String,
                     shortDescription: String,
                     comment: String
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
    val result = store.writeDetails(details, rtSite, rtCategory)
  }

  private def startDetails(site: RTSite,
                           category: RTCategory) = {

    //    store.getList().map(records => {
    //      records.foreach(r => {
    //        val url: String = r("url").toString
    //        scrapers ! ScrapDetails(url, rtS)
    //        Thread.sleep(2000) //throttle to not kill the site
    //      })
    //    })

  }


}
