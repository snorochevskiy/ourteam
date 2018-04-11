package dao

import javax.inject.Inject

import model.Company
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class CompanyDaoImpl @Inject()
(
  protected val dbConfigProvider: DatabaseConfigProvider
)
( implicit
  ec: ExecutionContext
) extends CompanyDao with OrganizationTables with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  override def all(): Future[Seq[Company]] = {
    val query = for { comany <- companies } yield comany
    db.run(query.result)
  }

  override def retrieve(id: String): Future[Option[Company]] = {
    val query = for {
      comany <- companies.filter(_.id === id)
    } yield comany
    db.run(query.result.headOption)
  }

  override def save(company: Company): Future[Company] = {
    val actions = (for {
      _ <- companies.insertOrUpdate(company)
    } yield ()).transactionally
    db.run(actions).map(_ => company)
  }
}
