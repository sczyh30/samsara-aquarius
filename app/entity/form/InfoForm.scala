package entity.form

import play.api.data.Form
import play.api.data.Forms._

case class InfoFormData(title: String,
                        url: String, cid: Int, updateDate: java.sql.Date)

/**
  * Info Insert Form Object
  */
object InfoForm extends FormTrait {

  val form = Form(
    mapping(
      "title" → nonEmptyText,
      "url" → nonEmptyText,
      "cid" → number,
      "updateDate" → sqlDate
    )(InfoFormData.apply)(InfoFormData.unapply)
  )
}
