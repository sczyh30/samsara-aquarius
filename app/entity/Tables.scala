package entity

/**
  * Samsara Aquarius
  * Tables Common Trait
  */
trait Tables {

  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.jdbc.{GetResult => GR}

  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
  /** Collection-like TableQuery object for table InfoData */
  lazy val InfoData = new TableQuery(tag => new InfoData(tag))
  /** Collection-like TableQuery object for table Comment */
  lazy val Comment = new TableQuery(tag => new Comment(tag))
  /** Collection-like TableQuery object for table Adm1n */
  lazy val Admin = new TableQuery(tag => new Admin(tag))

  /** DDL for all tables */
  lazy val schema: profile.SchemaDescription = User.schema ++ InfoData.schema ++ Comment.schema ++ Admin.schema


}
