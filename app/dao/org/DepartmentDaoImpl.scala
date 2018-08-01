package dao.org

import javax.inject.Inject
import model.Department
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class DepartmentDaoImpl @Inject()
(
  protected val dbConfigProvider: DatabaseConfigProvider
)
( implicit
  ec: ExecutionContext
) extends DepartmentDao with OrganizationTables with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  override def all(): Future[Seq[Department]] = {
    val query = for { department <- departments } yield department
    db.run(query.result)
  }

  override def retrieve(id: Int): Future[Option[Department]] = {
    val query = for {
      department <- departments.filter(_.id === id)
    } yield department
    db.run(query.result.headOption)
  }

  override def create(department: Department): Future[Department] = {
    val actions = (for {
      saved <- (departments returning departments.map(_.id) into ((dep,id) => dep.copy(id=Some(id)))) += department
    } yield saved).transactionally
    db.run(actions)
  }

  override def save(department: Department): Future[Department] = {
    val actions = (for {
      saved <- departments.insertOrUpdate(department)
    } yield saved).transactionally
    db.run(actions).map(_ => department)
  }

  override def byUserId(userId: String): Future[Option[Department]] =
    db run (for (
      employee <- employees if employee.userId === userId;
      team <- teams if team.id === employee.teamId;
      project <- projects if project.id === team.projectId;
      department <- departments if department.id === project.departmentId
    ) yield department).result.headOption
}
