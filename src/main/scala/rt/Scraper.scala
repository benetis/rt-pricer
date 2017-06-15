package rt

import akka.actor.{Actor, ActorRef}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

class Scraper(supervisor: ActorRef) extends Actor {

  override def receive: Receive = {
    case ScrapDetails(url, rtSite, rtCategory) =>
      println("=== start scraping details ===")
      println(s"url = $url")
      sender() ! StoreDetails(scrapDetails(url, rtSite, rtCategory), rtSite, rtCategory)
  }

  def scrapDetails(url: String,
                   rTSite: RTSite,
                   rTCategory: RTCategory): RTDetails = {
    val browser = new JsoupBrowser(
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")

    val doc = browser.get(url)

    println("=== [DETAILS] FLATS ===")
    val id = url.substring(url.length - 10, url.length - 1)
    val price = doc >> element(".obj-p" +
      "rice") >?> text
    val comment = doc >> element(".obj-comment") >?> text

    val detailsTerms = doc >> elementList(".obj-details dt")
    val detailsItem = doc >> elementList(".obj-details dd")

    val details = detailsTerms.zip(detailsItem)

    val splitDetails: Seq[RTDetailsArea] = this.splitDetails(details)

    val stats = doc >> elementList(".obj-stats dl dd")

    val created = stats(1).text
    val edited = stats(2).text
    val interested = stats(3).text


    RTDetails(Some(id), Some(url), price, splitDetails.head, None, None, None, None, None, None, comment,
      Some(created), Some(edited), Some(interested))
  }

  private def splitDetails(details: Seq[(Element, Element)]) = {
    lazy val _details = details.map({
      case (term, item) => term.text match {
        case "Area (m²):" => RTDetailsArea(convertAreaToDouble(item.text))
        case "Number of rooms :" => RTDetailsArea(convertAreaToDouble(item.text))
        case "Floor:" => RTDetailsArea(convertAreaToDouble(item.text))
        case "No. of floors:" => RTDetailsArea(convertAreaToDouble(item.text))
        case "Build year:" => RTDetailsArea(convertAreaToDouble(item.text))
        case "House Type:" => RTDetailsArea(convertAreaToDouble(item.text))
        case "Heating system:" => RTDetailsArea(convertAreaToDouble(item.text))
        case "Equipment:" => RTDetailsArea(convertAreaToDouble(item.text))
        case "Description:" => RTDetailsArea(convertAreaToDouble(item.text))
        case _ => RTDetailsArea(Some(1.0d))
      }
    })

    def convertAreaToDouble(area: String): Option[Double] = {
      Some(
        area
          .dropRight(3) // drop m^2
          .replace(",", ".") //to dots notation
          .toDouble
      )
    }

    _details
  }

}
