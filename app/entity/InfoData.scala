package entity

import slick.lifted.{Rep, Tag}

import slick.driver.MySQLDriver.api._

/**
  * Samsara Aquarius
  * Info Data row case class
  *
  * @author sczyh30
  */
case class InfoDataRow(id: Int, title: String = "No Title!",
                    url: String, cid: Int, updateDate: java.sql.Date)

class InfoData(tag: Tag) extends Table[InfoDataRow](tag, "info_data") {

  def * = (id, title, url, cid, updateDate) <> (InfoDataRow.tupled, InfoDataRow.unapply)

  /** Database column id SqlType(INT), AutoInc, PrimaryKey */
  val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
  /** Database column title SqlType(VARCHAR) */
  val title: Rep[String] = column[String]("title", O.Length(85,varying=true), O.Default("No Title!"))
  /** Database column url SqlType(VARCHAR) */
  val url: Rep[String] = column[String]("url", O.Length(150,varying=true))
  /** Database column cid SqlType(INT) */
  val cid: Rep[Int] = column[Int]("cid")
  /** Database column update_date SqlType(DATE) */
  val updateDate: Rep[java.sql.Date] = column[java.sql.Date]("update_date")
}
