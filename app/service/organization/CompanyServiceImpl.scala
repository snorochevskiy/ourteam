package service.organization
import javax.inject.Inject

import dao.CompanyDao
import model.Company

import scala.concurrent.{ExecutionContext, Future}

class CompanyServiceImpl @Inject()
(
  companyDao: CompanyDao
) extends CompanyService {

  override def retrieve(id: String): Future[Option[Company]] = companyDao.retrieve(id)

  override def save(company: Company): Future[Company] = companyDao.save(company)

}
