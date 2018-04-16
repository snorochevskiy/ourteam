package dao.org

import model.{Team}

import scala.concurrent.Future

trait TeamDao {
  def all(): Future[Seq[Team]]

  def retrieve(id: String): Future[Option[Team]]

  def save(team: Team): Future[Team]
}
