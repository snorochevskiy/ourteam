package dao

import com.mohiva.play.silhouette.api.LoginInfo
import model.{ContactInfo, DBLoginInfo, DBUserLoginInfo, User}
import slick.jdbc.JdbcProfile

trait UserTables {

  protected val driver: JdbcProfile
  import driver.api._

  class UserTable(tag: Tag) extends Table[User](tag, "DB_USER") {
    def id = column [String]("ID", O.PrimaryKey)
    def email = column[String]("EMAIL")
    def firstName = column[String]("FIRST_NAME")
    def lastName = column[String]("LAST_NAME")
    def middleName = column[String]("MIDDLE_NAME")
    def avatarURL = column[String]("AVATAR_URL")
    override def * = (id,email,firstName,lastName,middleName,avatarURL) <> (User.tupled, User.unapply)
  }
  val users = TableQuery[UserTable]


  class LoginInfos(tag: Tag) extends Table[DBLoginInfo](tag, "LOGIN_INFO") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def providerID = column[String]("PROVIDER_ID")
    def providerKey = column[String]("PROVIDER_KEY")
    def * = (id.?, providerID, providerKey) <> (DBLoginInfo.tupled, DBLoginInfo.unapply)
  }
  val loginInfos = TableQuery[LoginInfos]


  class UserLoginInfos(tag: Tag) extends Table[DBUserLoginInfo](tag, "USER_LOGIN_INFO") {
    def userID = column[String]("USER_ID")
    def loginInfoId = column[Long]("LOGIN_INFO_ID")
    def * = (userID, loginInfoId) <> (DBUserLoginInfo.tupled, DBUserLoginInfo.unapply)
  }
  val userLoginInfos = TableQuery[UserLoginInfos]

  // TODO: move to DAO
  def loginInfoQuery(loginInfo: LoginInfo) =
    loginInfos.filter(dbLoginInfo =>
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
  val passwordInfos = TableQuery[PasswordInfos]

  class ContactInfoTable(tag: Tag) extends Table[ContactInfo](tag, "USER_CONTACT_INFO") {
    def userId = column[String]("USER_ID")
    def location = column[String]("LOCATION")
    def phone1 = column[String]("PHONE1")
    def phone2 = column[String]("PHONE2")
    def phone3 = column[String]("PHONE3")
    def skype = column[String]("SKYPE")
    def telegram = column[String]("TELEGRAM")
    def viber = column[String]("VIBER")
    def line = column[String]("LINE")
    override def * = (userId,location,phone1,phone2,phone3,skype,telegram,viber,line) <>
      (ContactInfo.tupled, ContactInfo.unapply)
  }
  val contactInfos = TableQuery[ContactInfoTable]
}
