package message

import akka.actor.Actor
import akka.actor.Actor.Receive

trait FavoriteAct
case class FavoriteOn(uid: Int, aid: Int) extends FavoriteAct
case class FavoriteOff(uid: Int, aid: Int) extends FavoriteAct

/**
  * Samsara Aquarius Message System
  * Favorite Message Actor
  *
  * @author sczyh30
  * @since 0.3.0
  */
class FavoriteMessageActor extends Actor {

  override def receive: Receive = {
    case FavoriteOn(u, a) =>
      ???
    case FavoriteOff(u, a) =>
      ???
    case _ =>
      sender ! "unknown_msg"
  }

}
