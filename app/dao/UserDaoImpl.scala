package dao

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import model.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserDaoImpl @Inject()
(
  protected val dbConfigProvider: DatabaseConfigProvider
) extends UserDao with UserTables with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._ // required for ===

  override def find(loginInfo: LoginInfo): Future[Option[User]] = {
    val userQuery = for {
      dbLoginInfo <- loginInfoQuery(loginInfo)
      dbUserLoginInfo <- slickUserLoginInfos.filter(_.loginInfoId === dbLoginInfo.id)
      dbUser <- slickUsers.filter(_.id === dbUserLoginInfo.userID)
    } yield dbUser
    db.run(userQuery.result.headOption).map { dbUserOption =>
      dbUserOption.map { dbUser =>
        User(dbUser.id, loginInfo, dbUser.email, dbUser.firstName, dbUser.lastName, dbUser.middleName, dbUser.avatarURL)
      }
    }
  }

  override def find(userId: String): Future[Option[User]] = {
    val query = for {
      dbUser <- slickUsers.filter(_.id === userId.toString)
      dbUserLoginInfo <- slickUserLoginInfos.filter(_.userID === dbUser.id)
      dbLoginInfo <- slickLoginInfos.filter(_.id === dbUserLoginInfo.loginInfoId)
    } yield (dbUser, dbLoginInfo)
    db.run(query.result.headOption).map { resultOption =>
      resultOption.map {
        case (dbUser, loginInfo) =>
          User(
            dbUser.id,
            LoginInfo(loginInfo.providerID, loginInfo.providerKey),
            dbUser.email,
            dbUser.firstName,
            dbUser.lastName,
            dbUser.middleName,
            dbUser.avatarURL
          )
      }
    }
  }


  override def save(user: User): Future[User] = {
    val dbUser = DbUser(user.id, user.email, user.firstName, user.lastName, user.middleName, user.avatarUrl)
    val dbLoginInfo = DBLoginInfo(None, user.loginInfo.providerID, user.loginInfo.providerKey)
    // We don't have the LoginInfo id so we try to get it first.
    // If there is no LoginInfo yet for this user we retrieve the id on insertion.
    val loginInfoAction = {
      val retrieveLoginInfo = slickLoginInfos.filter(
        info => info.providerID === user.loginInfo.providerID &&
          info.providerKey === user.loginInfo.providerKey).result.headOption
      val insertLoginInfo = slickLoginInfos.returning(slickLoginInfos.map(_.id)).
        into((info, id) => info.copy(id = Some(id))) += dbLoginInfo
      for {
        loginInfoOption <- retrieveLoginInfo
        loginInfo <- loginInfoOption.map(DBIO.successful(_)).getOrElse(insertLoginInfo)
      } yield loginInfo
    }
    // combine database actions to be run sequentially
    val actions = (for {
      _ <- slickUsers.insertOrUpdate(dbUser)
      loginInfo <- loginInfoAction
      _ <- slickUserLoginInfos += DBUserLoginInfo(dbUser.id, loginInfo.id.get)
    } yield ()).transactionally
    // run actions and return user afterwards
    db.run(actions).map(_ => user)
  }

}
