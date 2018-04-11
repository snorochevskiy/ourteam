package dao

import com.mohiva.play.silhouette.api.LoginInfo
import model.{Company, Department}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

trait UserTables {

  protected val driver: JdbcProfile
  import driver.api._

  // We can't persist loginInfo = LoginInfo from Emploee class using Slick,
  // that's why this class is required
  // https://github.com/sbrunk/play-silhouette-slick-seed/blob/master/app/models/daos/DBTableDefinitions.scala
  case class DbUser(id: String, email: String, firstName:String, lastName: String, middleName: String,
    avatarURL: String)

  class UserTable(tag: Tag) extends Table[DbUser](tag, "DB_USER") {
    def id = column [String]("ID", O.PrimaryKey)
    def email = column[String]("EMAIL")
    def firstName = column[String]("FIRST_NAME")
    def lastName = column[String]("LAST_NAME")
    def middleName = column[String]("MIDDLE_NAME")
    def avatarURL = column[String]("AVATAR_URL")
    override def * = (id,email,firstName,lastName,middleName,avatarURL) <> (DbUser.tupled, DbUser.unapply)
  }

  val slickUsers = TableQuery[UserTable]

  case class DBLoginInfo (id: Option[Long], providerID: String, providerKey: String)

  class LoginInfos(tag: Tag) extends Table[DBLoginInfo](tag, "LOGIN_INFO") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def providerID = column[String]("PROVIDER_ID")
    def providerKey = column[String]("PROVIDER_KEY")
    def * = (id.?, providerID, providerKey) <> (DBLoginInfo.tupled, DBLoginInfo.unapply)
  }

  val slickLoginInfos = TableQuery[LoginInfos]

  case class DBUserLoginInfo (userID: String, loginInfoId: Long)

  class UserLoginInfos(tag: Tag) extends Table[DBUserLoginInfo](tag, "USER_LOGIN_INFO") {
    def userID = column[String]("USER_ID")
    def loginInfoId = column[Long]("LOGIN_INFO_ID")
    def * = (userID, loginInfoId) <> (DBUserLoginInfo.tupled, DBUserLoginInfo.unapply)
  }

  val slickUserLoginInfos = TableQuery[UserLoginInfos]

  // TODO: move to DAO
  def loginInfoQuery(loginInfo: LoginInfo) =
    slickLoginInfos.filter(dbLoginInfo =>
      dbLoginInfo.providerID === loginInfo.providerID && dbLoginInfo.providerKey === loginInfo.providerKey)

  case class DBPasswordInfo (
    hasher: String,
    password: String,
    salt: Option[String],
    loginInfoId: Long
  )

  class PasswordInfos(tag: Tag) extends Table[DBPasswordInfo](tag, "PASSWORD_INFO") {
    def hasher = column[String]("HASHER")
    def password = column[String]("PASSWORD")
    def salt = column[Option[String]]("SALT")
    def loginInfoId = column[Long]("LOGIN_INFO_ID")
    def * = (hasher, password, salt, loginInfoId) <> (DBPasswordInfo.tupled, DBPasswordInfo.unapply)
  }
  val slickPasswordInfos = TableQuery[PasswordInfos]

  // TODO: add contact info
  //def phone1 = column[String]("PHONE1")
  //def phone2 = column[String]("PHONE2")
  //def phone3 = column[String]("PHONE3")
  //def hangouts = column[String]("HANGOUTS")
  //def skype = column[String]("SKYPE")
  //def telegram = column[String]("TELEGRAM")
  //def viber = column[String]("VIBER")
  //def line = column[String]("LINE)

}
