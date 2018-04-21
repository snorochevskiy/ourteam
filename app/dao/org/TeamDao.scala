package dao.org

import model.{Project, Team}

import scala.concurrent.Future

trait TeamDao {
  def all(): Future[Seq[Team]]

  def retrieve(id: Int): Future[Option[Team]]

  def save(team: Team): Future[Team]

  def byEmployee(userId: String): Future[Option[(Team)]]

  def retrieveWithProject(id: Int): Future[Option[(Team, Project)]]

  def byProject(projectId: Int): Future[Seq[Team]]
}
