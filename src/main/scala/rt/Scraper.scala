package rt

import akka.actor.{Actor, ActorRef}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.element

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

    val priceEleOpt = doc >?> element(".obj-price")

    priceEleOpt match { // if page exists
      case Some(priceEle) =>
        val (price, pricePerMeter) = parsePrice(priceEle)

        val comment = doc >> element(".obj-comment") >?> text

        val detailsTerms = doc >> elementList(".obj-details dt")
        val detailsItem = doc >> elementList(".obj-details dd")

        val itemDetails = detailsTerms.zip(detailsItem)

        lazy val parsedItemDetails: Seq[Any] = this.splitDetails(itemDetails)

        val stats = doc >> elementList(".obj-stats dl dd")

        val created = stats(1).text
        val edited = stats(2).text
        val interested = stats(3).text


        RTItem(
          Some(id)
          , Some(url)
          , price
          , pricePerMeter
          , parsedItemDetails.collectFirst { case d: RTDetailsArea => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsNumberOfRooms => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsFloor => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsNumberOfFloors => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsBuildYear => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsHouseType => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsHeatingSystem => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsEquipment => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsShortDescription => d.value }
          , comment
          , Some(created)
          , Some(edited)
          , Some(interested)
        )
      case None => RTItem(Some(id), None, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None)
    }


  }

  private def parsePrice(price: => Element)
  : (Option[Double], Option[Double]) = {

    def extractPricePerMeter(priceWithoutAdvert: Option[String]): Option[Double] = {

      val capturePricePerMeterWithWhiteSpace = "\\([0-9 ]+".r

      val matched = capturePricePerMeterWithWhiteSpace
        .findFirstIn(priceWithoutAdvert.getOrElse("-1"))

      matched
        .map(_.replace(" ", ""))
        .map(_.replace("(", ""))
        .map(_.toDouble)

    }

    def extractPriceWithoutCurrency(priceWithoutAdvert: Option[String]): Option[Double] = {
      val capturePriceWithoutCurrencyWithWhiteSpace = "^\\s?[0-9 ]+".r

      val matched = capturePriceWithoutCurrencyWithWhiteSpace
        .findFirstIn(priceWithoutAdvert.getOrElse("-1"))

      matched
        .map(_.replace(" ", ""))
        .map(_.toDouble)
    }

    def extractPriceWithoutAdvert: Option[String] = {
      val priceAdvert: Option[String] = price >?> element(".price-change") match {
        case Some(ele: Element) => ele >?> text
        case None => None
      }

      val rawPriceOpt = price >?> text

      rawPriceOpt match {
        case Some(rawPrice: String) => priceAdvert match {
          case Some(advert: String) => Some(rawPrice.replace(advert, ""))
          case None => Some(rawPrice)
        }
        case None => None
      }
    }

    val priceWithoutAdvert = extractPriceWithoutAdvert

    val pricePerMeter = extractPricePerMeter(priceWithoutAdvert)

    val priceWithoutCurrency = extractPriceWithoutCurrency(priceWithoutAdvert)

    (
      priceWithoutCurrency,
      pricePerMeter
    )
  }

  private def splitDetails(details: Seq[(Element, Element)]): Seq[Any]

  = {
    lazy val _details = details.map({
      case (term, item) => term.text match {
        case "Area (m²):" => convertAreaToDouble(item.text)
        case "Number of rooms :" => RTDetailsNumberOfRooms(item.text.toInt)
        case "Floor:" => RTDetailsFloor(item.text.toInt)
        case "No. of floors:" => RTDetailsNumberOfRooms(item.text.toInt)
        case "Build year:" => RTDetailsBuildYear(item.text.toInt)
        case "House Type:" => RTDetailsHouseType(item.text)
        case "Heating system:" => RTDetailsHeatingSystem(item.text)
        case "Equipment:" => RTDetailsEquipment(item.text)
        case "Description:" => RTDetailsShortDescription(item.text)
        case _ => 1.0d
      }
    })

    def convertAreaToDouble(area: String): Option[RTDetailsArea] = {
      Some({
        val areaStr = area
          .dropRight(3) // drop m^2
          .replace(",", ".") //to dots notation

        if (areaStr.isEmpty)
          RTDetailsArea(0.0d)
        else
          RTDetailsArea(areaStr.toDouble)
      })
    }

    _details
  }

}
