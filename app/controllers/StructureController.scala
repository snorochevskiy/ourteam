package controllers

import javax.inject._

import dao.{CompanyDao, Dao}
import play.api._
import play.api.mvc._

import scala.concurrent.ExecutionContext


@Singleton
class StructureController @Inject() (companyDao: CompanyDao, cc: ControllerComponents)
                               (implicit executionContext: ExecutionContext)
  extends AbstractController(cc) {


  def listCompanies() = Action.async {
    companyDao.all().map { case companies => Ok(views.html.companies(companies)) }

  }

}
