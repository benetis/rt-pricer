import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

object Main {
  val browser = JsoupBrowser()
  val doc = browser.get("https://google.lt")

  println()
  println("=== OBSERVADOR ===")

  doc >> extractor(".logo img", attr("src")) |> println
  doc >> extractor("meta[name=description]", attr("content")) |> println

  println("==================")
  println()

  doc >> ".small-news-list h4 > a" foreach println
}
