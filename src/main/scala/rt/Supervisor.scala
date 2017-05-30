package rt

import akka.actor.{Actor, ActorRef, ActorSystem, _}

import java.net.URL

case class Start(url: String)
case class Scrap(url: String)

class Supervisor(system: ActorSystem) extends Actor {

  var host2Actor = Map.empty[String, ActorRef]
  var numVisited = 0

  val scrapers = system.actorOf(Props(new Scraper(self)))

  override def receive: Receive = {
    case Start(url) => scrapers ! Scrap(url)
    case _ => "Not handled"
  }



}
