package dao.org

import javax.inject.Inject
import model.{Team}
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

  override def all(): Future[Seq[Team]] = {
    val query = for { team <- teams } yield team
    db.run(query.result)
  }

  override def retrieve(id: String): Future[Option[Team]] = {
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

}
