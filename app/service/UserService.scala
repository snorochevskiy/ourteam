package service

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import model.UserIdentity

import scala.concurrent.Future

trait UserService extends IdentityService[UserIdentity] {

  def retrieve(id: String): Future[Option[UserIdentity]]

  def retrieve(loginInfo: LoginInfo): Future[Option[UserIdentity]]

  def save(user: UserIdentity): Future[UserIdentity]

}
