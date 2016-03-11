package entity

import slick.lifted.{Rep, Tag}

import slick.driver.MySQLDriver.api._

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

/**
  * Info Data Table
  * @param tag Tag
  */
class InfoDataTable(tag: Tag) extends Table[InfoData](tag, "info_data") {

  override def * = (id, title, url, cid, updateDate) <> (InfoData.tupled, InfoData.unapply)

  /** Database column id SqlType(INT), AutoInc, PrimaryKey */
  val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
  /** Database column title SqlType(VARCHAR) */
  val title: Rep[String] = column[String]("title", O.Length(85, varying=true), O.Default("No Title!"))
  /** Database column url SqlType(VARCHAR) */
  val url: Rep[String] = column[String]("url", O.Length(150, varying=true))
  /** Database column cid SqlType(INT) */
  val cid: Rep[Int] = column[Int]("cid")
  /** Database column update_date SqlType(DATE) */
  val updateDate: Rep[java.sql.Date] = column[java.sql.Date]("update_date")
}
