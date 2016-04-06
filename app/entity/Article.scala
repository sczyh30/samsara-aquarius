package entity

/**
  * Samsara Aquarius
  * Article row case class
  *
  * @param id article id
  * @param title article title
  * @param url article url
  * @param cid article category
  * @param updateDate article update date
  */
case class Article(id: Int, title: String = "No Title!", author: String,
                   url: String, cid: Int, updateDate: java.sql.Date)
