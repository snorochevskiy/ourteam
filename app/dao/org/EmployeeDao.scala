package dao.org

import model.{ContactInfo, Employee, User}

import scala.concurrent.Future

trait EmployeeDao {
  def all(): Future[Seq[Employee]]

  def retrieve(id: String): Future[Option[Employee]]

  def teamMembers(teamId: Int): Future[Seq[(Employee, User, Option[ContactInfo])]]

  def save(employee: Employee): Future[Employee]
}
