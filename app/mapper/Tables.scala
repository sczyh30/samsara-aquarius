package mapper

object Tables extends {
  val profile = slick.driver.MySQLDriver
} with Tables

/**
  * Samsara Aquarius
  * Tables Common Trait
  */
trait Tables {
  val profile: slick.driver.JdbcProfile

  import entity._

  import profile.api._

  import slick.lifted.{TableQuery, Rep, Tag}

  /**
    * Comment Table
    *
    * @param tag Tag
    */
  class CommentTable(tag: Tag) extends Table[Comment](tag, "comment") {

    override def * = (cid, uid, dataId, text, time) <> (entity.Comment.tupled, entity.Comment.unapply)

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

    /** Foreign key referencing InfoData (database name fk_cm_dataid) */
    lazy val infoDataFk = foreignKey("fk_cm_dataid", dataId, InfoData)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing User (database name fk_cm_uid) */
    lazy val userFk = foreignKey("fk_cm_uid", uid, User)(r => r.uid, onUpdate = ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.NoAction)
  }

  /**
    * User Table
    *
    * @param tag Rag
    */
  class UserTable(tag: Tag) extends Table[User](tag, "user") {

    override def * = (uid, username, password, joinDate, avatar, tips, website) <> (entity.User.tupled, entity.User.unapply)

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

  /**
    * Info Data Table
    *
    * @param tag Tag
    */
  class InfoDataTable(tag: Tag) extends Table[InfoData](tag, "info_data") {

    override def * = (id, title, url, cid, updateDate) <> (entity.InfoData.tupled, entity.InfoData.unapply)

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

  /**
    * Admin Table
    *
    * @param tag Tag
    */
  class AdminTable(tag: Tag) extends Table[Admin](tag, "adm1n") {
    // id: PrimaryKey
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)

    val name: Rep[String] = column[String]("name", O.Length(45, varying = true))

    val password: Rep[String] = column[String]("password", O.Length(110,varying=true))

    val role: Rep[Option[Int]] = column[Option[Int]]("role", O.Default(Some(1)))

    val email: Rep[String] = column[String]("email", O.Length(65,varying=true))

    val index1 = index("name_UNIQUE", name, unique=true)

    override def * = (id, name, password, role, email) <> (entity.Admin.tupled, entity.Admin.unapply)

  }

  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new UserTable(tag))

  /** Collection-like TableQuery object for table InfoData */
  lazy val InfoData = new TableQuery(tag => new InfoDataTable(tag))

  /** Collection-like TableQuery object for table Comment */
  lazy val Comment = new TableQuery(tag => new CommentTable(tag))

  /** Collection-like TableQuery object for table Adm1n */
  lazy val Admin = new TableQuery(tag => new AdminTable(tag))

  /** DDL for all tables */
  lazy val schema: profile.SchemaDescription = User.schema ++ InfoData.schema ++ Comment.schema ++ Admin.schema

}

