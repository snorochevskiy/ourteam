package service.organization
import dao.org.EmployeeDao
import javax.inject.Inject
import model.Employee

import scala.concurrent.{ExecutionContext, Future}

class EmployeeServiceImpl @Inject()
(
  employeeDao: EmployeeDao
)
( implicit
  ec: ExecutionContext
)extends EmployeeService {

  override def retrieve(userId: String): Future[Option[Employee]] = employeeDao.retrieve(userId)

  override def save(employee: Employee): Future[Employee] = employeeDao.save(employee)

}
