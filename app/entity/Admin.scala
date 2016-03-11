package entity

import slick.lifted.{Rep, Tag}

import slick.driver.MySQLDriver.api._

/**
  * Samsara Aquarius
  * Admin row case class
  *
  * @author sczyh30
  */
case class AdminRow(id: Int, name: String, password: String,
                    role: Option[Int] = Some(1), email: String)

/**
  * Admin Table
  * @param tag Tag
  */
class Admin(tag: Tag) extends Table[AdminRow](tag, "adm1n") {
  // id: PrimaryKey
  val id: Rep[Int] = column[Int]("id", O.PrimaryKey)

  val name: Rep[String] = column[String]("name", O.Length(45, varying = true))

  val password: Rep[String] = column[String]("password", O.Length(110,varying=true))

  val role: Rep[Option[Int]] = column[Option[Int]]("role", O.Default(Some(1)))

  val email: Rep[String] = column[String]("email", O.Length(65,varying=true))

  val index1 = index("name_UNIQUE", name, unique=true)

  def * = (id, name, password, role, email) <> (AdminRow.tupled, AdminRow.unapply)

}