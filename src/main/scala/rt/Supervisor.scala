package rt

import akka.actor.{Actor, ActorRef, ActorSystem, _}

import java.net.URL

case class StartList(rtSite: RTSite)
case class ScrapList(url: String)

class Supervisor(system: ActorSystem) extends Actor {

  var host2Actor = Map.empty[String, ActorRef]
  var numVisited = 0

  val scrapers = system.actorOf(Props(new Scraper(self)))

  override def receive: Receive = {
    case StartList(rtSite: RTSite) => scrapers ! ScrapList(rtSite.nextPage(RTFlatsRent(), 1))
    case _ => "Not handled"
  }



}
