package rt

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions
import sorm._

object Db extends Instance(
  entities = Set(Entity[RTItem]()),
  url = "jdbc:mysql://127.0.0.1:3306/rt-prices",
  user = "dev",
  password = "root9191"
)

object Store {

  def writeDetails(item: RTItem,
                   rtSite: RTSite,
                   rtCategory: RTCategory): Future[Boolean] = {

    val time = System.currentTimeMillis()

    Db.save(
      item
    )


    /*val point = Point("RTItem", )
      .addTag("category", rtSite.categoryId(rtCategory))
      .addField("category", rtSite.categoryId(rtCategory))
      .addField("id", item.id.getOrElse(""))
      .addField("url", item.url.getOrElse(""))
      .addTag("price", item.price)
      .addField("price", item.price)
      .addTag("price-per-meter", item.pricePerMeter)
      .addField("price-per-meter", item.pricePerMeter)
      .addTag("area", item.area)
      .addField("area", item.area)
      .addTag("rooms", item.rooms)
      .addField("rooms", item.rooms)
      .addField("floor", item.floor)
      .addField("number-of-floors", item.numberOfFloors)
      .addField("build-year", item.buildYear)
      .addTag("house-type", item.houseType)
      .addField("house-type", item.houseType)
      .addField("heating-system", item.heatingSystem)
      .addField("equipment", item.equipment)
      .addField("short-description", item.shortDescription)
      .addField("comment", item.comment.getOrElse(""))
      .addTag("created", item.created.getOrElse("not-specified"))
      .addField("created", item.created.getOrElse(""))
      .addField("edited", item.edited.getOrElse(""))
      .addField("interested", item.interested.getOrElse(""))*/
      Future(true)
  }

}
