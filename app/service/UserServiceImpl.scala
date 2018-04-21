package service

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import dao.UserDao
import model.UserIdentity

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl @Inject()(userDao: UserDao)(implicit ex: ExecutionContext)extends UserService {

  override def retrieve(id: String): Future[Option[UserIdentity]] = userDao.find(id)

  override def retrieve(loginInfo: LoginInfo): Future[Option[UserIdentity]] = userDao.find(loginInfo)

  override def save(user: UserIdentity): Future[UserIdentity] = userDao.save(user)
}
