package rt

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.actor.Actor.Receive
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._


class Scraper(supervisor: ActorRef) extends Actor {

  override def receive: Receive = {
    case ScrapDetails(url, rtSite, rtCategory) =>
      println("=== start scraping details ===")
      println(s"url = $url")
      val result = scrapDetails(url, rtSite, rtCategory)
      sender() ! StoreDetails(result, rtSite, rtCategory)
  }

  def scrapDetails(url: String, rTSite: RTSite, rTCategory: RTCategory): RTDetails = {
    val browser = new JsoupBrowser("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")

    val doc = browser.get(url)

    println("=== [DETAILS] FLATS ===")
    val id = url.substring(url.length - 10, url.length - 1)
    val price = doc >> element(".obj-price") >?> text

    RTDetails(Some(id),Some(url),Some(price),None,None,None,None,None,None,None,None)
  }

}
