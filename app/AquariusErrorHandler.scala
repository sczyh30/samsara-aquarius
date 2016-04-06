import javax.inject.{Provider, Inject}

import play.api.mvc.RequestHeader
import play.api.mvc.Results._
import play.api.routing.Router
import play.api.{OptionalSourceMapper, Configuration, Environment}
import play.api.http.DefaultHttpErrorHandler

import scala.concurrent.Future

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
}
