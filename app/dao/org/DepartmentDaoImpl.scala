package dao.org

import javax.inject.Inject
import model.{Department}
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

  override def save(department: Department): Future[Department] = {
    val actions = (for {
      _ <- departments.insertOrUpdate(department)
    } yield ()).transactionally
    db.run(actions).map(_ => department)
  }
}
