package dao

import com.mohiva.play.silhouette.api.LoginInfo
import model.UserIdentity

import scala.concurrent.Future

trait UserDao {

  def find(loginInfo: LoginInfo): Future[Option[UserIdentity]]

  def find(id: String): Future[Option[UserIdentity]]

  def save(user: UserIdentity): Future[UserIdentity]
}
