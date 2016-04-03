package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, Controller}
import utils.GeetestLib

/**
  * Captcha Controller
  */
@Singleton
class CaptchaController extends Controller {

  val captcha_id = "sec"
  val private_key = "sec"

  def init = Action { implicit request =>
    val gtSdk = new GeetestLib(captcha_id, private_key)
    val status = gtSdk.preProcess()
    Ok(gtSdk.getResponseStr) withSession((gtSdk.gtServerStatusSessionKey, status.toString))
  }

}
