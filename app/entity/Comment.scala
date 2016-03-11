package entity

import slick.lifted.{Rep, Tag}

import slick.driver.MySQLDriver.api._
import slick.model.ForeignKeyAction
import entity.User

/**
  * Samsara Aquarius
  * Comment row case class
  *
  * @author sczyh30
  */
case class CommentRow(cid: Int, uid: Int, dataId: Int, text: String)

/**
  * Comment Table
  * @param tag Tag
  */
class Comment(tag: Tag) extends Table[CommentRow](tag, "comment") {

  def * = (cid, uid, dataId, text, time) <> (CommentRow.tupled, CommentRow.unapply)

  /** Database column cid SqlType(INT), AutoInc, PrimaryKey */
  val cid: Rep[Int] = column[Int]("cid", O.AutoInc, O.PrimaryKey)
  /** Database column uid SqlType(INT) */
  val uid: Rep[Int] = column[Int]("uid")
  /** Database column data_id SqlType(INT) */
  val dataId: Rep[Int] = column[Int]("data_id")
  /** Database column text SqlType(VARCHAR), Length(150,true) */
  val text: Rep[String] = column[String]("text", O.Length(150,varying=true))
  /** Database column time SqlType(DATETIME) */
  val time: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("time")

  lazy val User = new TableQuery(tag => new User(tag))
  lazy val InfoData = new TableQuery(tag => new InfoData(tag))

  /** Foreign key referencing InfoData (database name fk_cm_dataid) */
  lazy val infoDataFk = foreignKey("fk_cm_dataid", dataId, InfoData)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  /** Foreign key referencing User (database name fk_cm_uid) */
  lazy val userFk = foreignKey("fk_cm_uid", uid, User)(r => r.uid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
}
