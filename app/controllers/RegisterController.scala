package controllers

import javax.inject.Inject

import play.api.mvc._

/**
  * Samsara Aquarius Route
  * Register Controller
  * @author sczyh30
  */
class RegisterController @Inject() extends Controller{
  def index = Action {
    Ok(views.html.register())
  }
}
