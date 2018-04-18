package controllers

import auth.DefaultEnv
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import dao.org.TeamDao
import javax.inject._
import model.{Employee, Team, User}
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc._
import service.organization.{EmployeeService, ProjectService}

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class OrganizationController @Inject()
(
  employeeService: EmployeeService,
  teamDao: TeamDao,
  projectService: ProjectService,
  cc: ControllerComponents,
  silhouette: Silhouette[DefaultEnv]
)
( implicit
  ec: ExecutionContext
) extends AbstractController(cc) with I18nSupport {


  def me() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val user: User = request.identity
    employeeService.retrieve(user.id).flatMap{
      case maybeEmployee: Option[Employee] => Future.successful(Ok(views.html.employee(user, maybeEmployee)))
    }.recover {
      case e: Exception =>
        Ok(views.html.error()).flashing("error" -> e.getMessage)
    }
  }

  def myTeam() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val user: User = request.identity
    teamDao.byEmployee(user.id).flatMap{
      case Some((team, project)) => Future.successful(Ok(views.html.team(team, project)))
      case None => Future.successful(Ok(views.html.error()).flashing("error" -> Messages("team.notFound")))
    }.recover {
      case e: Exception =>
        Ok(views.html.error()).flashing("error" -> Messages("team.notFound"))
    }
  }

  def projectInfo(projectId: Int) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    projectService.fullInfo(projectId).flatMap {
      case Some((project, department, teams)) => Future.successful(Ok(views.html.project(project, department, teams)))
      case None => Future.successful(Ok(views.html.error()).flashing("error" -> Messages("project.notFound")))
    }.recover {
      case e: Exception =>
        Ok(views.html.error()).flashing("error" -> Messages("team.notFound"))
    }
  }

}
