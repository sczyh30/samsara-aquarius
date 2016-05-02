import javax.inject.{Inject, Provider}

import play.api.mvc.RequestHeader
import play.api.mvc.Results._
import play.api.routing.Router
import play.api._
import play.api.http.{DefaultHttpErrorHandler, HttpErrorHandlerExceptions}

import scala.concurrent.Future
import scala.util.control.NonFatal

/**
  * Samsara Aquarius
  * Request Error Handler
  *
  * @since 0.2.2
  */
class AquariusErrorHandler @Inject()(env: Environment,
                                     config: Configuration,
                                     sourceMapper: OptionalSourceMapper,
                                     router: Provider[Router])
  extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  override def onNotFound(request: RequestHeader, message: String) = {
    Future.successful(
      NotFound(views.html.error.NotFound()(request))
    )
  }

  override def onBadRequest(request: RequestHeader, message: String) = {
    Future.successful(NotFound(views.html.error.NotFound()(request))) // 400 -> 404
  }

  override def onServerError(request: RequestHeader, exception: Throwable) = {
    try {
      val usefulException = HttpErrorHandlerExceptions.throwableToUsefulException(sourceMapper.sourceMapper,
        env.mode == Mode.Prod, exception)

      logServerError(request, usefulException)

      env.mode match {
        case Mode.Prod => onProdServerError(request)
        case _ => onDevServerError(request, usefulException)
      }
    } catch {
      case NonFatal(e) =>
        Logger.error("Error while handling error", e)
        Future.successful(InternalServerError)
    }
  }

  def onProdServerError(request: RequestHeader) = {
    Future.successful(InternalServerError(views.html.error.ServerError("Oops...", "服务器出了点小问题。。。")(request)))
  }
}
