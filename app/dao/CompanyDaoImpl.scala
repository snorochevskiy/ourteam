package dao

import javax.inject.Inject

import model.Company
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

class CompanyDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends CompanyDao with Dao {

  import profile.api._

  override def all(): Future[Seq[Company]] = {
    val query = for { comany <- companies } yield comany
    db.run(query.result)
  }

}
