package entity

/**
  * Samsara Aquarius
  * User row case class
  *
  * @author sczyh30
  *
  * @param uid user id
  * @param username username
  * @param password password of user
  * @param joinDate the join date
  * @param avatar avatar id
  * @param tips personal introduction
  * @param website personal website
  * @param email personal email
  */
case class User(uid: Int, username: String, password: String,
                joinDate: java.sql.Date,
                avatar: Option[String] = Some("default"),
                tips: Option[String] = Some("None~"),
                website: Option[String] = Some(""),
                email: String)




