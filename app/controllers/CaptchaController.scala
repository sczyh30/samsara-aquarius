package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, Controller}
import utils.captcha.GeetestConfig
import utils.captcha.GeetestLib

/**
  * Samsara Aquarius Route
  * Captcha Controller
  *
  * @author sczyh30
  */
@Singleton
class CaptchaController extends Controller {

  /**
    * Samsara Aquarius Application
    * Captcha Init Process
    */
  def init = Action { implicit request =>
    val gtSdk = new GeetestLib(GeetestConfig.getCaptchaId, GeetestConfig.getPrivateKey)
    val status = gtSdk.preProcess()
    Ok(gtSdk.getResponseStr) withSession(request.session + (gtSdk.gtServerStatusSessionKey -> status.toString))
  }

}
