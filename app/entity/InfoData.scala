package entity

/**
  * Samsara Aquarius
  * Info Data row case class
  *
  * @param id info id
  * @param title info title
  * @param url info url
  * @param cid info category
  * @param updateDate info update date
  */
case class InfoData(id: Int, title: String = "No Title!",
                    url: String, cid: Int, updateDate: java.sql.Date)

