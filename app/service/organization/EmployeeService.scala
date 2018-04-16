package service.organization

import model.{Employee}

import scala.concurrent.Future

trait EmployeeService {

  def retrieve(userId: String): Future[Option[Employee]]

  def save(employee: Employee): Future[Employee]

}
