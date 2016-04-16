package base.action

import controllers.routes
import play.api.mvc.Results._
import play.api.mvc.{ActionBuilder, Request, Result}

import scala.concurrent.Future

/**
  * Samsara Aquarius
  * Authenticated Action for Play
  *
  * @since 0.5.16
  */
object AuthenticatedAction extends ActionBuilder[Request] {

  override def invokeBlock[A](request: Request[A],
                              block: (Request[A]) => Future[Result]): Future[Result] = {
    utils.DateUtils.ensureSession(request)
    request.session.get("aq_token") match {
      case Some(u) =>
        block(request)
      case None =>
        Future.successful(Redirect(routes.UserController.loginIndex()))
    }
  }
}
