package entity.form

import play.api.data.Form
import play.api.data.Forms._

case class InfoFormData(title: String,
                        url: String, author: String, cid: Int, updateDate: java.sql.Date)

/**
  * Article Insert Form Object
  */
object InfoForm extends FormTrait {

  val form = Form(
    mapping(
      "title" → nonEmptyText(minLength = 8),
      "url" → nonEmptyText(maxLength = 150),
      "author" → nonEmptyText(maxLength = 45),
      "cid" → number(min = 0),
      "updateDate" → sqlDate
    )(InfoFormData.apply)(InfoFormData.unapply)
  )
}
