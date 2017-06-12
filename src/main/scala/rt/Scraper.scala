package rt

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.actor.Actor.Receive
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._


class Scraper(supervisor: ActorRef) extends Actor {

  override def receive: Receive = {
    case ScrapList(url, rTSite, rTCategory) =>
      println("=== start scraping list of items===")
      println(s"url = $url")
      val result = scrap(url)
      sender() ! StoreList(result, rTSite, rTCategory)
    case ScrapDetails(url) =>
      println("=== start scraping details ===")
      println(s"url = $url")
      val result = scrapDetails(url)
//      sender() ! StoreDetails(result)
  }

  def scrap(url: String): Seq[Option[String]] = {

    val browser = new JsoupBrowser("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")

    val doc = browser.get(url)


    println("=== FLATS IN KAUNAS ===")
    doc >> elementList(".list-row a") >?> attr("href")
  }

  def scrapDetails(url: String): List[RTDetails] = {
    val browser = new JsoupBrowser("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")

    val doc = browser.get(url)

    println("=== [DETAILS] FLATS IN KAUNAS ===")
    val result = doc >> element(".obj-price") >?> text
    result |> println
    List()
  }

}
