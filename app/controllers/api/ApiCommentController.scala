package controllers.api

import javax.inject.{Inject, Singleton}

import play.api.mvc.Controller
import service.CommentService

/**
  * Samsara Aquarius Route
  * REST API Comment Controller
  *
  * @author sczyh30
  */
@Singleton
class ApiCommentController @Inject() (service: CommentService) extends Controller {

}
