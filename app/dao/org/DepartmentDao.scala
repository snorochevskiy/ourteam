package dao.org

import model.{Department}

import scala.concurrent.Future

trait DepartmentDao {
  def all(): Future[Seq[Department]]

  def retrieve(id: Int): Future[Option[Department]]

  def create(department: Department): Future[Department]

  def save(department: Department): Future[Department]

  def byUserId(userId: String): Future[Option[Department]]
}
