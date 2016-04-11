package entity.form

import play.api.data.Form
import play.api.data.Forms._

case class RegisterFormData(username: String, password: String, email: String,
                            geetest_challenge: String, geetest_validate: String,
                            geetest_seccode: String)

/**
  * Register Form Object
  */
object RegisterForm extends FormTrait {

  override val form = Form(
    mapping(
      "username" → nonEmptyText(minLength = 4, maxLength = 14),
      "password" → nonEmptyText(minLength = 8, maxLength = 16),
      "email" → email,
      "geetest_challenge" → nonEmptyText,
      "geetest_validate" → nonEmptyText,
      "geetest_seccode" → nonEmptyText
    )(RegisterFormData.apply)(RegisterFormData.unapply)
  )
}
