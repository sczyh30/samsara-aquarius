package entity

import scala.language.implicitConversions

/**
  * Samsara Aquarius
  * User row case class
  *
  * @author sczyh30
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
                avatar: Option[String] = Some("default.png"),
                tips: Option[String] = Some("无"),
                website: Option[String] = Some(""),
                email: String)

case class UserInfo(uid: Int, username: String, joinDate: java.sql.Date,
                    avatar: Option[String] = Some("default.png"),
                    tips: Option[String] = Some("无"),
                    website: Option[String] = Some(""))

object UserInfo {
  implicit def convert(user: User): UserInfo =
    UserInfo(user.uid, user.username, user.joinDate, user.avatar, user.tips, user.website)
}




