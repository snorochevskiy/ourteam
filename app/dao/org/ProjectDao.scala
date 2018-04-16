package dao.org

import model.{Project}

import scala.concurrent.Future

trait ProjectDao {
  def all(): Future[Seq[Project]]

  def retrieve(id: String): Future[Option[Project]]

  def save(project: Project): Future[Project]
}
