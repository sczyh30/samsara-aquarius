package entity

/**
  * Samsara Aquarius
  * User row case class
  *
  * @author sczyh30
  */
case class User(uid: Int, username: String, password: String,
                joinDate: java.sql.Date, avatar: Option[String] = Some("default"),
                tips: Option[String] = Some("None~"), website: String)




