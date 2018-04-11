package controllers

import javax.inject.Inject

import auth.DefaultEnv
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import scala.concurrent.{ExecutionContext, Future}
import forms.CompanyForms._
import model.Company
import play.api.i18n.{I18nSupport, Messages}
import service.organization.CompanyService

class CompanyMgmtController @Inject()
(
  companyService: CompanyService,
  cc: ControllerComponents,
  silhouette: Silhouette[DefaultEnv]
)
( implicit
  ec: ExecutionContext
) extends AbstractController(cc) with I18nSupport {


  def createCompanyPage() = silhouette.SecuredAction { implicit request: Request[AnyContent] =>
    Ok(views.html.createCompany(companyForm))
  }

  def createCompanySubmit() = silhouette.SecuredAction.async { implicit request: Request[AnyContent] =>

    val form = companyForm.bindFromRequest

    form.fold(
      err => {
        Future.successful(BadRequest(views.html.createCompany(form)))
      },
      companyData => {
        // TODO: Add validation and persisting

        for {
          _ <- companyService.save(Company(companyData.id, companyData.name))
          result <- Future.successful(Redirect(routes.OrganizationController.listCompanies()))
        } yield result

      }.recover {
        case e: Exception =>
          // TODO: how to pass fields values also? Like BadRequest(views.html.createCompany(companyForm))
          Redirect(routes.CompanyMgmtController.createCompanyPage()).flashing("error" -> e.getMessage)
      }
    )

  }
}
