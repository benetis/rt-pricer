package rt

import akka.actor.{Actor, ActorRef, ActorSystem, _}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.{attr, elementList}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import java.net.URL

case class Scrap(url: String)

class Supervisor(system: ActorSystem) extends Actor {

  var host2Actor = Map.empty[String, ActorRef]
  var numVisited = 0

  override def receive: Receive = {
    case Scrap(url) => scrap(url)
    case _ => "Not handled"
  }


  def scrap(url: String) = {

    val browser = new JsoupBrowser("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")

    val doc = browser.get(url)

    println("=== STARTING ===")
    println(s"url = $url")

    println("=== FLATS IN KAUNAS ===")
    val flats = doc >> elementList(".list-row a") >?> attr("href")
    flats |> println

  }



}
