package service

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import dao.UserDao
import model.User

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl @Inject()(employeeDao: UserDao)(implicit ex: ExecutionContext)extends UserService {

  override def retrieve(id: String): Future[Option[User]] = employeeDao.find(id)

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = employeeDao.find(loginInfo)

  override def save(employee: User): Future[User] = employeeDao.save(employee)
}
