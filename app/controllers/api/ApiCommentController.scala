package controllers.api

import javax.inject.{Inject, Singleton}

import play.api.mvc.Controller
import service.CommentService

/**
  * Samsara Aquarius API v1
  * Comment API Controller
  *
  * @author sczyh30
  */
@Singleton
class ApiCommentController @Inject() (service: CommentService) extends Controller {

  def fetchByArticle = TODO

  def comment = TODO

  def remove = TODO

}
