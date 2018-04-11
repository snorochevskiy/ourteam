package service.organization

import model.Company

import scala.concurrent.Future

trait CompanyService {

  def retrieve(id: String): Future[Option[Company]]

  def save(company: Company): Future[Company]

}
