package dao.org

import model.{Employee}

import scala.concurrent.Future

trait EmployeeDao {
  def all(): Future[Seq[Employee]]

  def retrieve(id: String): Future[Option[Employee]]

  def save(employee: Employee): Future[Employee]
}
