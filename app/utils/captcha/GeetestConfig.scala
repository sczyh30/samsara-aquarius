package utils.captcha

import com.typesafe.config.{ConfigFactory, Config}

import scala.language.postfixOps
import scala.util.Try

/**
  *  Geetest Config
  */
object GeetestConfig {

  val config = ConfigFactory.load("captcha")

  def getCaptchaId: String = Try {
    config.getString("geetest.captcha_id")
  } recover {
    case _ => ""
  } get

  def getPrivateKey: String = Try {
    config.getString("geetest.private_key")
  } recover {
    case _ => ""
  } get
}
