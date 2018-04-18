package service.organization

import dao.org.{ProjectDao, TeamDao}
import javax.inject.Inject
import model.{Department, Project, Team}

import scala.concurrent.{ExecutionContext, Future}

class ProjectServiceImpl @Inject()
(
  projectDao: ProjectDao,
  teamDao: TeamDao,
)
( implicit
    ec: ExecutionContext
) extends ProjectService {

  override def fullInfo(projectId: Int): Future[Option[(Project, Department, Seq[Team])]] = {
    val projectFuture = projectDao.retrieveWithDepartment(projectId)
    val teamsFuture = teamDao.byProject(projectId)

    for (project <- projectFuture; teams <- teamsFuture) yield project match {
      case Some((proj, dep)) => Some((proj, dep, teams))
      case None => None
    }
  }

}
