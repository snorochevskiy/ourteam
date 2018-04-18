package dao.org

import javax.inject.Inject
import model.{Project, Team}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class TeamDaoImpl @Inject()
(
  protected val dbConfigProvider: DatabaseConfigProvider
)
( implicit
  ec: ExecutionContext
) extends TeamDao with OrganizationTables with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  override def all(): Future[Seq[Team]] = db run teams.result

  override def retrieve(id: Int): Future[Option[Team]] = {
    val query = for {
      team <- teams.filter(_.id === id)
    } yield team
    db.run(query.result.headOption)
  }

  override def save(team: Team): Future[Team] = {
    val actions = (for {
      _ <- teams.insertOrUpdate(team)
    } yield ()).transactionally
    db.run(actions).map(_ => team)
  }

  override def byEmployee(userId: String): Future[Option[(Team, Project)]] = {
    val query = for {
      employee <- employees.filter(_.userId === userId)
      team <- teams.filter(_.id === employee.teamId)
      project <- projects.filter(_.id === team.projectId)
    } yield (team, project)
    db.run(query.result.headOption)
  }

  override def byProject(projectId: Int): Future[Seq[Team]] = db run teams.filter(_.projectId === projectId).result
}
