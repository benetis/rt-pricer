package rt

import akka.actor.{Actor, ActorRef, ActorSystem, _}

import java.net.URL

case class Start(rtSite: RTAruodas)
case class Scrap(url: String)
case class ScrapList(url: List[String])

class Supervisor(system: ActorSystem) extends Actor {

  var host2Actor = Map.empty[String, ActorRef]
  var numVisited = 0

  val scrapers = system.actorOf(Props(new Scraper(self)))

  override def receive: Receive = {
    case Start(rtSite: RTAruodas) => scrapers ! Scrap(rtSite.nextPage(RTFlatsRent()))
    case _ => "Not handled"
  }



}
