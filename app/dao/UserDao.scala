package dao

import com.mohiva.play.silhouette.api.LoginInfo
import model.User

import scala.concurrent.Future

trait UserDao {

  def find(loginInfo: LoginInfo): Future[Option[User]]

  def find(id: String): Future[Option[User]]

  def save(user: User): Future[User]
}
