package controllers

import auth.DefaultEnv
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import javax.inject._
import dao.org.CompanyDao
import model.{Employee, User}
import play.api.i18n.Messages
import play.api.mvc._
import service.organization.EmployeeService

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class OrganizationController @Inject()
(
  employeeService: EmployeeService,
  cc: ControllerComponents,
  silhouette: Silhouette[DefaultEnv]
)
( implicit
  ec: ExecutionContext
) extends AbstractController(cc) {


  def me() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    println(request.identity)
    val user: User = request.identity
    employeeService.retrieve(user.id).flatMap{
      case maybeEmployee:Option[Employee] => Future.successful(Ok(views.html.employee(user, maybeEmployee)))
    }.recover {
      case e: Exception =>
        // TODO: create dedicated error page? Embed error header to main veiew?
        Redirect(routes.DashboardController.index()).flashing("error" -> e.getMessage)
    }
  }

}
