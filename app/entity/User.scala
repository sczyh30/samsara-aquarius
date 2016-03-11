package entity

import slick.lifted.{Rep, Tag}

import slick.driver.MySQLDriver.api._

/**
  * Samsara Aquarius
  * User row case class
  *
  * @author sczyh30
  */
case class UserRow(uid: Int, username: String, password: String,
                joinDate: java.sql.Date, avatar: Option[String] = Some("default"),
                tips: Option[String] = Some("None~"), website: String)

/**
  * User Table
  * @param tag Rag
  */
class User(tag: Tag) extends Table[UserRow](tag, "user") {

  def * = (uid, username, password, joinDate, avatar, tips, website) <> (UserRow.tupled, UserRow.unapply)

  /** Database column uid SqlType(INT), AutoInc, PrimaryKey */
  val uid: Rep[Int] = column[Int]("uid", O.AutoInc, O.PrimaryKey)
  /** Database column username SqlType(VARCHAR) */
  val username: Rep[String] = column[String]("username", O.Length(20, varying=true))
  /** Database column password SqlType(VARCHAR) */
  val password: Rep[String] = column[String]("password", O.Length(90, varying=true))
  /** Database column join_date SqlType(DATE) */
  val joinDate: Rep[java.sql.Date] = column[java.sql.Date]("join_date")
  /** Database column avatar SqlType(VARCHAR) */
  val avatar: Rep[Option[String]] = column[Option[String]]("avatar", O.Length(65, varying=true), O.Default(Some("default")))
  /** Database column tips SqlType(VARCHAR) */
  val tips: Rep[Option[String]] = column[Option[String]]("tips", O.Length(100, varying=true), O.Default(Some("None~")))
  /** Database column website SqlType(VARCHAR) */
  val website: Rep[String] = column[String]("website", O.Length(65, varying=true))

  /** Uniqueness Index over (username) */
  val index1 = index("username_UNIQUE", username, unique=true)
}


