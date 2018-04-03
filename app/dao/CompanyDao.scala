package dao

import model.Company

import scala.concurrent.Future

trait CompanyDao {

  def all(): Future[Seq[Company]]
}
