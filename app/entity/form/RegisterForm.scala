package entity.form

import play.api.data.Form
import play.api.data.Forms._

case class RegisterFormData(username: String, password: String, email: String)

/**
  * Register Form Object
  */
object RegisterForm extends FormTrait {

  override val form = Form(
    mapping(
      "username" → nonEmptyText(minLength = 4, maxLength = 14),
      "password" → nonEmptyText(minLength = 8, maxLength = 16),
      "email" → email
    )(RegisterFormData.apply)(RegisterFormData.unapply)
  )
}
