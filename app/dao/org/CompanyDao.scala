package dao.org

import model.Company

import scala.concurrent.Future

trait CompanyDao {

  def all(): Future[Seq[Company]]

  def retrieve(id: String): Future[Option[Company]]

  def save(company: Company): Future[Company]
}
