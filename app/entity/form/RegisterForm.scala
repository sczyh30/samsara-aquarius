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
      "username" → nonEmptyText,
      "password" → nonEmptyText,
      "email" → nonEmptyText
    )(RegisterFormData.apply)(RegisterFormData.unapply)
  )
}
