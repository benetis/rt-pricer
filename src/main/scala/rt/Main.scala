package rt

import akka.actor.{ActorSystem, PoisonPill, Props}
import com.sun.tools.javadoc.Start

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App {

  val system = ActorSystem()
  val supervisor = system.actorOf(Props(new Supervisor(system)))

  val flatKaunas = "https://www.aruodas.lt/butai/kaune/?FDistrict=6&obj=1&FOrder=Actuality&FRegion=43&mod=Siulo&act=makeSearch&Page=1"

  supervisor ! Start(flatKaunas)

  Await.result(system.whenTerminated, 10 minutes)

  supervisor ! PoisonPill
  system.terminate
}
