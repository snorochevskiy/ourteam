package dao.org

import model.{Department, Project, Team}

import scala.concurrent.Future

trait ProjectDao {
  def all(): Future[Seq[Project]]

  def retrieve(id: Int): Future[Option[Project]]

  def retrieveWithDepartment(id: Int):Future[Option[(Project, Department)]]

  def save(project: Project): Future[Project]
}
