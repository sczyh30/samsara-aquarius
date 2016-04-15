package base.action

import controllers.routes
import play.api.mvc.Results._
import play.api.mvc.{ActionBuilder, Request, Result}

import scala.concurrent.Future

/**
  * Samsara Aquarius
  */
object MustBeAdminGo extends ActionBuilder[Request] {

  override def invokeBlock[A](request: Request[A],
                              block: (Request[A]) => Future[Result]): Future[Result] = {
    utils.DateUtils.ensureSession("adm1n_go_token", 20)(request)
    request.session.get("adm1n_go_token") match {
      case Some(u) =>
        block(request)
      case None =>
        Future.successful(Redirect(routes.Application.index()))
    }
  }
}
