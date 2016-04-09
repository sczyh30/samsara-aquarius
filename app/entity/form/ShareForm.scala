package entity.form

import play.api.data.Form
import play.api.data.Forms._

case class ShareFormData(title: String, url: String, geetest_challenge: String,
                         geetest_validate: String, geetest_seccode: String)

/**
  * Share Form Object
  */
object ShareForm extends FormTrait {

  override val form = Form(
    mapping(
      "title" → nonEmptyText(maxLength = 80),
      "url" → nonEmptyText(maxLength = 150),
      "geetest_challenge" → nonEmptyText,
      "geetest_validate" → nonEmptyText,
      "geetest_seccode" → nonEmptyText
    )(ShareFormData.apply)(ShareFormData.unapply)
  )
}
