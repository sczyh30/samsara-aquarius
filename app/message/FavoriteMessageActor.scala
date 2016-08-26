package message

import java.sql.Timestamp
import java.time.LocalDateTime

import akka.actor.{Props, Actor}
import entity.Results._
import akka.actor.Actor.Receive
import entity.Favorite
import service.FavoriteService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions
import scala.util.{Failure, Success}


object FavoriteMessageActor {

  //def props = Props(Class[FavoriteMessageActor], service)

  trait FavoriteAct
  case class FavoriteOn(aid: Int, uid: Int) extends FavoriteAct
  case class FavoriteOff(aid: Int, uid: Int) extends FavoriteAct

  implicit def convertOn(o: FavoriteOn): Favorite =
    Favorite(o.aid, o.uid, Some(Timestamp.valueOf(LocalDateTime.now())))

  implicit def convertOff(o: FavoriteOff): Favorite =
    Favorite(o.aid, o.uid, Some(Timestamp.valueOf(LocalDateTime.now())))
}

/**
  * Favorite message actor for the Samsara Aquarius Message System.
  *
  * @author sczyh30
  * @since 0.3.0
  */
class FavoriteMessageActor (service: FavoriteService) extends Actor {

  import FavoriteMessageActor._

  override def receive: Receive = {
    case go @ FavoriteOn(a, u) =>
      val sender = super.sender()
      service ❤ go onComplete { // like
        case Success(x) =>
          sender ! FAVORITE_PROCESS_SUCCESS
        case Failure(ex) =>
          sender ! FAVORITE_PROCESS_FAIL
      }
    case shit @ FavoriteOff(a, u) =>
      val sender = super.sender()
      service !♡! shit map { res => // cancel like
        if (res > 0) sender ! CANCEL_FAVORITE_PROCESS_SUCCESS
        else         sender ! FAVORITE_PROCESS_FAIL
      }
    case _ =>
      sender ! RECV_UNKNOWN_MSG
  }

}
