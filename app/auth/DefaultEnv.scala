package auth

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import model.UserIdentity

trait DefaultEnv extends Env {
  type I = UserIdentity
  type A = CookieAuthenticator
}
