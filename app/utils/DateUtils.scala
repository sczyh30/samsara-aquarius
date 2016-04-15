package utils

import java.time.{Duration, LocalDateTime}

import play.api.mvc.{AnyContent, Request}

/**
  * Samsara Aquarius Utils
  * Date Utils
  *
  * @author sczyh30
  */
object DateUtils {

  val InfiniteDateString = "2099-01-24T05:16:27"

  def ensureSession(implicit request: Request[_]): Unit = {
    val timestamp = LocalDateTime.parse(request.session.get("timestamp")
      .getOrElse(InfiniteDateString))
    val dur = Duration.between(timestamp, LocalDateTime.now()).toMinutes
    if (dur > 30)
      request.session - "aq_token" - "username" - "uid" - "timestamp"
    else
      request.session + ("timestamp", LocalDateTime.now().toString)
  }
}
