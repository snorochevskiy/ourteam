package service

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import model.User

import scala.concurrent.Future

trait UserService extends IdentityService[User] {

  def retrieve(id: String): Future[Option[User]]

  def retrieve(loginInfo: LoginInfo): Future[Option[User]]

  def save(employee: User): Future[User]

}
