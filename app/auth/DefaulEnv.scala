package auth

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import model.User

trait DefaultEnv extends Env {
  type I = User
  type A = CookieAuthenticator
}
