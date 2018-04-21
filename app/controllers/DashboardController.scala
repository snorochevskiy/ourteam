package controllers

import javax.inject._

import auth.DefaultEnv
import com.mohiva.play.silhouette.api.Silhouette
import play.api._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DashboardController @Inject()
(
  cc: ControllerComponents,
  silhouette: Silhouette[DefaultEnv]
)
(implicit
   executionContext: ExecutionContext
) extends AbstractController(cc) {

  def index() = silhouette.SecuredAction { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

}
