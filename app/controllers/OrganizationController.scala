package controllers

import auth.DefaultEnv
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import dao.org.{DepartmentDao, EmployeeDao, ProjectDao, TeamDao}
import javax.inject.{Singleton, Inject}
import model.{Department, Project, Employee, Team, UserIdentity}
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms.{boolean, mapping, number, optional, text}
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc._
import service.organization.{EmployeeService, ProjectService}

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class OrganizationController @Inject()
(
  employeeDao: EmployeeDao,
  employeeService: EmployeeService,
  teamDao: TeamDao,
  projectDao: ProjectDao,
  projectService: ProjectService,
  departmentDao: DepartmentDao,
  cc: ControllerComponents,
  silhouette: Silhouette[DefaultEnv]
)
( implicit
  ec: ExecutionContext
) extends AbstractController(cc) with I18nSupport {

  val departmentForm = Form(
    mapping(
      "id" -> optional(number),
      "code" -> text,
      "name" -> text,
      "description" -> text
    )(Department.apply)(Department.unapply)
  )

  val projectForm = Form(
    mapping(
      "id" -> optional(number),
      "departmentId" -> number,
      "code" -> text,
      "name" -> text,
      "description" -> text
    )(Project.apply)(Project.unapply)
  )

  def me() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val user: UserIdentity = request.identity
    employeeService.retrieve(user.id).map {
      case maybeEmployee: Option[Employee] => Ok(views.html.employee(user, maybeEmployee))
    }.recover {
      case e: Exception =>
        Ok(views.html.error()).flashing("error" -> e.getMessage)
    }
  }

  def myProject() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val user: UserIdentity = request.identity

    projectDao.byUserId(user.id).map {
      case Some((project)) => Redirect(controllers.routes.OrganizationController.projectInfo(project.id.get))
      case None => Ok(views.html.error()).flashing("error" -> Messages("project.notFound"))
    }
  }

  def createProject(departmentId: Int) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.editProject(new Project(None, departmentId, "", "", ""))))
  }

  def submitProject() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    projectForm.bindFromRequest.fold(
      formError => {
        Logger.warn(formError.errors.map(_.message).reduceLeft(_ concat _))
        Future.successful(BadRequest(views.html.error())
          .flashing("error" -> formError.errors.map(_.message).reduceLeft(_ concat _)))
      },
      project => {
        Logger.info(s"Department to save: $project")
        projectDao.create(project).map(proj => Redirect(routes.OrganizationController.projectInfo(proj.id.get)))
      }
    )
  }

  def myTeam() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val user: UserIdentity = request.identity

    teamDao.byEmployee(user.id).map {
      case Some((team)) => Redirect(controllers.routes.OrganizationController.teamInfo(team.id.get))
      case None => Ok(views.html.error()).flashing("error" -> Messages("team.notFound"))
    }.recover {
      case e: Exception =>
        Ok(views.html.error()).flashing("error" -> Messages("team.notFound"))
    }
  }

  def myDepartment() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val user: UserIdentity = request.identity

    departmentDao.byUserId(user.id).map {
      case Some((project)) => Redirect(controllers.routes.OrganizationController.departmentInfo(project.id.get))
      case None => Ok(views.html.error()).flashing("error" -> Messages("department.notFound"))
    }
  }

  def listDepartments() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    for (departments <- departmentDao.all()) yield Ok(views.html.departments(departments))
  }


  def departmentInfo(departmentId: Int) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    for (department <- departmentDao.retrieve(departmentId)) yield department match {
      case Some(dep) => Ok(views.html.departmentInfo(dep))
      case None      => Ok(views.html.error()).flashing("error" -> Messages("team.notFound"))
    }
  }

  def departmentProjects(departmentId: Int) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    for (projects <- projectDao.byDepartmentId(departmentId)) yield Ok(views.html.projects(departmentId, projects))
  }

  def createDepartment() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.editDepartment(new Department(None, "", "", ""))))
  }

  def submitDepartment() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    departmentForm.bindFromRequest.fold(
      formError => {
        Logger.warn(formError.errors.map(_.message).reduceLeft(_ concat _))
        Future.successful(BadRequest(views.html.editDepartment(new Department(None, "", "", "")))
          .flashing("error" -> formError.errors.map(_.message).reduceLeft(_ concat _)))
      },
      department => {
        Logger.info(s"Department to save: $department")
        departmentDao.create(department).map(dep => Redirect(routes.OrganizationController.departmentInfo(dep.id.get)))
      }
    )
  }

  def teamInfo(teamId: Int) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    teamDao.retrieveWithProject(teamId).map {
      case Some((team, project)) => Ok(views.html.team(team, project))
      case None => Ok(views.html.error()).flashing("error" -> Messages("team.notFound"))
    }.recover {
      case e: Exception =>
        Ok(views.html.error()).flashing("error" -> Messages("team.notFound"))
    }
  }

  def projectInfo(projectId: Int) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    projectService.fullInfo(projectId).map {
      case Some((project, department, teams)) => Ok(views.html.project(project, department, teams))
      case None => Ok(views.html.error()).flashing("error" -> Messages("project.notFound"))
    }.recover {
      case e: Exception =>
        Ok(views.html.error()).flashing("error" -> Messages("team.notFound"))
    }
  }

  def teamMembers(teamId: Int) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    for {
      teamMembers <- employeeDao.teamMembers(teamId)
    } yield Ok(views.html.teamMembers(teamMembers))
  }

}
