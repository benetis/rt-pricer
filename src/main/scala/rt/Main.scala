package rt

import akka.actor.{ActorSystem, PoisonPill, Props}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App {

  val system = ActorSystem()
  val supervisor = system.actorOf(Props(new Supervisor(system)))

//  supervisor ! StartList(RTAruodas())
  supervisor ! StartDetails(RTAruodas(), RTFlatsSell())

  Await.result(system.whenTerminated, 10 minutes)

  supervisor ! PoisonPill
  system.terminate
}
