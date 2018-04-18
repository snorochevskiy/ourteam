package dao.org

import javax.inject.Inject
import model.{Department, Project, Team}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class ProjectDaoImpl @Inject()
(
  protected val dbConfigProvider: DatabaseConfigProvider
)
(implicit
  ec: ExecutionContext
) extends ProjectDao with OrganizationTables with HasDatabaseConfigProvider[JdbcProfile]
{
  import profile.api._

  override def all(): Future[Seq[Project]] = {
    val query = for { project <- projects } yield project
    db.run(query.result)
  }

  override def retrieve(id: Int): Future[Option[Project]] = {
    val query = for {
      project <- projects.filter(_.id === id)
    } yield project
    db.run(query.result.headOption)
  }

  override def save(project: Project): Future[Project] = {
    val actions = (for {
      _ <- projects.insertOrUpdate(project)
    } yield ()).transactionally
    db.run(actions).map(_ => project)
  }

  override def retrieveWithDepartment(id: Int): Future[Option[(Project, Department)]] =
    db run (for (
      project <- projects if project.id === id;
      department <- departments if department.id === project.departmentId
    ) yield (project, department)).result.headOption

}
