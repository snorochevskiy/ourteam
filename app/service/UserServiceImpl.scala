package service

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import dao.UserDao
import model.User

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl @Inject()(userDao: UserDao)(implicit ex: ExecutionContext)extends UserService {

  override def retrieve(id: String): Future[Option[User]] = userDao.find(id)

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userDao.find(loginInfo)

  override def save(user: User): Future[User] = userDao.save(user)
}
