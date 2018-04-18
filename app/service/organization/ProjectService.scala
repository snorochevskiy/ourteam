package service.organization

import model.{Department, Project, Team}

import scala.concurrent.Future

trait ProjectService {

  def fullInfo(projectId: Int): Future[Option[(Project, Department, Seq[Team])]]
}
