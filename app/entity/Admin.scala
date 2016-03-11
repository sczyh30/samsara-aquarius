package entity

/**
  * Samsara Aquarius
  * Admin case class
  *
  * @author sczyh30
  *
  * @param id admin id
  * @param name admin name
  * @param password admin password
  * @param role admin role(level)
  * @param email admin's email
  */
case class Admin(id: Int, name: String, password: String,
                    role: Option[Int] = Some(1), email: String)

