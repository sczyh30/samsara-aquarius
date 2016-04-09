package entity

/**
  * Samsara Aquarius
  * Article share case class
  *
  * @author sczyh30
  * @since 0.2.22
  *
  * @param sid share id, auto increment
  * @param title article title
  * @param url article url
  * @param user user id, if not login could be default 0
  */
case class Share(sid: Int, title: String, url: String, user: Option[Int] = Some(0))
