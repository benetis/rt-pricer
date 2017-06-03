package rt

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.actor.Actor.Receive
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._


class Scraper(supervisor: ActorRef) extends Actor {

  override def receive: Receive = {
    case ScrapList(url) =>
      println("=== start scraping ===")
      println(s"url = $url")
      scrap(url) |> println
  }

  def scrap(url: String): Seq[Option[String]] = {

    val browser = new JsoupBrowser("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")

    val doc = browser.get(url)


    println("=== FLATS IN KAUNAS ===")
    doc >> elementList(".list-row a") >?> attr("href")
  }

}
