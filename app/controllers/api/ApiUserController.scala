package controllers.api

import javax.inject.{Inject, Singleton}

import play.api.mvc.Controller
import service.UserService

/**
  * Samsara Aquarius API v1
  * User Controller
  *
  * @author sczyh30
  */
@Singleton
class ApiUserController @Inject() (service: UserService) extends Controller {

  def register() = TODO

  def login() = TODO

  def fetch(uid: Int) = TODO

  def update() = TODO

}
