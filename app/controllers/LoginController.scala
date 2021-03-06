package controllers

import javax.inject.{Inject, Singleton}
import auth.DefaultEnv
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.Authenticator.Implicits._
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.{LoginEvent, Silhouette}
import com.mohiva.play.silhouette.api.util.{Clock, Credentials}
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.providers._
import play.api.Configuration
import net.ceedubs.ficus.Ficus._
import org.webjars.play.WebJarsUtil
import play.api.data.Form
import play.api.data.Forms.{boolean, mapping, number, text}
import play.api.data.validation.Constraints._
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import service.UserService

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration

@Singleton
class LoginController @Inject() (
  silhouette: Silhouette[DefaultEnv],
  userService: UserService,
  credentialsProvider: CredentialsProvider,
  configuration: Configuration,
  clock: Clock,
  cc: ControllerComponents
) (implicit
   webJarsUtil: WebJarsUtil,
   assets: AssetsFinder,
   ex: ExecutionContext
)
extends AbstractController(cc) with I18nSupport {

  case class LoginFormData(login: String, passwd: String, rememberMe: Boolean)

  val loginForm = Form(
    mapping(
      "login" -> text,
      "passwd" -> text,
      "rememberMe" -> boolean
    )(LoginFormData.apply)(LoginFormData.unapply)
  )

  def loginPage() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login())
  }

  def submitLogin() = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest.fold(
      formError => {
        Future.successful(BadRequest(views.html.login()))
      },
      loginData => {
        val credentials = Credentials(loginData.login, loginData.passwd)
        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
          val result = Redirect(routes.DashboardController.index())
          userService.retrieve(loginInfo).flatMap {
            case Some(user) =>
              val c = configuration.underlying
              silhouette.env.authenticatorService.create(loginInfo).map {
                case authenticator if loginData.rememberMe =>
                  authenticator.copy(
                    expirationDateTime = clock.now + c.as[FiniteDuration]("silhouette.authenticator.rememberMe.authenticatorExpiry"),
                    idleTimeout = c.getAs[FiniteDuration]("silhouette.authenticator.rememberMe.authenticatorIdleTimeout"),
                    cookieMaxAge = c.getAs[FiniteDuration]("silhouette.authenticator.rememberMe.cookieMaxAge")
                  )
                case authenticator => authenticator
              }.flatMap { authenticator =>
                silhouette.env.eventBus.publish(LoginEvent(user, request))
                silhouette.env.authenticatorService.init(authenticator).flatMap { v =>
                  silhouette.env.authenticatorService.embed(v, result)
                }
              }
            case None =>
              Future.failed(new IdentityNotFoundException("Couldn't find user"))
          }
        }.recover {
          case _: ProviderException =>
            Redirect(routes.LoginController.loginPage()).flashing("error" -> Messages("invalid.credentials"))
        }
      }
    )
  }

  def signOut = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val result = Redirect(routes.LoginController.loginPage())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }

}
