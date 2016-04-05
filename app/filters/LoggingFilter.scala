package filters

import javax.inject.Inject

import akka.stream.Materializer
import play.api.Logger
import play.api.mvc.{Result, RequestHeader, Filter}
import play.api.routing.Router.Tags
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Samsara Aquarius
  * Logging Filter
  */
class LoggingFilter @Inject() (implicit val mat: Materializer) extends Filter {

  override def apply(f: (RequestHeader) => Future[Result])
                    (rh: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis

    f(rh).map { result =>

      val action = rh.tags(Tags.RouteController) +
        "." + rh.tags(Tags.RouteActionMethod)
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime

      Logger.info(s"$action took ${requestTime}ms and returned ${result.header.status}")

      //result.withHeaders("Request-Time" -> requestTime.toString)
      result
    }
  }
  
}
