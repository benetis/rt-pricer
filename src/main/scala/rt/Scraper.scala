package rt

import akka.actor.{Actor, ActorRef}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

import scala.util.Try

class Scraper(supervisor: ActorRef) extends Actor {

  override def receive: Receive = {
    case ScrapDetails(url, rtSite, rtCategory) =>
      println("=== start scraping details ===")
      println(s"url = $url")
      sender() ! StoreDetails(scrapDetails(url, rtSite, rtCategory), rtSite, rtCategory)
  }

  def scrapDetails(url: String,
                   rTSite: RTSite,
                   rTCategory: RTCategory): RTItem = {
    val browser = new JsoupBrowser(
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")

    val doc = browser.get(url)

    println("=== [DETAILS] FLATS ===")
    val id = url.substring(url.length - 10, url.length - 1)

    val price = parsePrice(doc >> element(".obj-price") >?> text)

    val comment = doc >> element(".obj-comment") >?> text

    val detailsTerms = doc >> elementList(".obj-details dt")
    val detailsItem = doc >> elementList(".obj-details dd")

    val itemDetails = detailsTerms.zip(detailsItem)

    val parsedItemDetails: Seq[RTDetails] = this.splitDetails(itemDetails)

    val stats = doc >> elementList(".obj-stats dl dd")

    val created = stats(1).text
    val edited = stats(2).text
    val interested = stats(3).text


    RTItem(
      Some(id)
      , Some(url)
      , price
      , parsedItemDetails.collect { case d: RTDetailsArea => d}.headOption
      , parsedItemDetails.collect { case d: RTDetailsNumberOfRooms => d}.headOption
      , parsedItemDetails.collect { case d: RTDetailsFloor => d}.headOption
      , parsedItemDetails.collect { case d: RTDetailsNumberOfFloors => d}.headOption
      , parsedItemDetails.collect { case d: RTDetailsBuildYear => d}.headOption
      , parsedItemDetails.collect { case d: RTDetailsHouseType => d}.headOption
      , parsedItemDetails.collect { case d: RTDetailsHeatingSystem => d}.headOption
      , parsedItemDetails.collect { case d: RTDetailsEquipment => d}.headOption
      , parsedItemDetails.collect { case d: RTDetailsShortDescription => d}.headOption
      , comment
      , Some(created)
      , Some(edited)
      , Some(interested)
    )
  }

  private def parsePrice(price: Option[String]): Option[RTDetailsPrice] = {
    Some(RTDetailsPrice(price))
  }

  private def splitDetails(details: Seq[(Element, Element)]): Seq[RTDetails] = {
    lazy val _details = details.map({
      case (term, item) => term.text match {
        case "Area (m²):" => RTDetailsArea(convertAreaToDouble(item.text))
        case "Number of rooms :" => RTDetailsNumberOfRooms(Try(item.text.toInt).toOption)
        case "Floor:" => RTDetailsFloor(Try(item.text.toInt).toOption)
        case "No. of floors:" => RTDetailsNumberOfRooms(Try(item.text.toInt).toOption)
        case "Build year:" => RTDetailsBuildYear(Try(item.text.toInt).toOption)
        case "House Type:" => RTDetailsHouseType(Try(item.text).toOption)
        case "Heating system:" => RTDetailsHeatingSystem(Try(item.text).toOption)
        case "Equipment:" => RTDetailsEquipment(Try(item.text).toOption)
        case "Description:" => RTDetailsShortDescription(Try(item.text).toOption)
        case _ => RTDetailsArea(Some(1.0d))
      }
    })

    def convertAreaToDouble(area: String): Option[Double] = {
      Some({
        val areaStr = area
          .dropRight(3) // drop m^2
          .replace(",", ".") //to dots notation

        if (areaStr.isEmpty)
          0.0d
        else
          areaStr.toDouble
      })
    }

    _details
  }

}
