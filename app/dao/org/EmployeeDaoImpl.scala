package dao.org

import javax.inject.Inject
import model.{Employee}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class EmployeeDaoImpl  @Inject()
(
  protected val dbConfigProvider: DatabaseConfigProvider
)
( implicit
  ec: ExecutionContext
) extends EmployeeDao with OrganizationTables with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  override def all(): Future[Seq[Employee]] = {
    val query = for { employee <- employees } yield employee
    db.run(query.result)
  }

  override def retrieve(userId: String): Future[Option[Employee]] = {
    val query = for {
      employee <- employees.filter(_.userId === userId)
    } yield employee
    db.run(query.result.headOption)
  }

  override def save(employee: Employee): Future[Employee] = {
    val actions = (for {
      _ <- employees.insertOrUpdate(employee)
    } yield ()).transactionally
    db.run(actions).map(_ => employee)
  }
}
