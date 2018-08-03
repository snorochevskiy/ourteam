package model

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

case class User(id: String, email: String, firstName:String, lastName: String, middleName: String, avatarURL: String)

case class ContactInfo
(
  userId: String, location: String, phone1: String, phone2: String, phone3: String,
  skype: String, telegram: String, viber: String, line: String
)

case class UserIdentity
(
  id: String, loginInfo: LoginInfo, email: String, firstName:String, lastName: String, middleName: String,
  avatarUrl: String
) extends Identity
case class DBLoginInfo (id: Option[Long], providerID: String, providerKey: String)
case class DBUserLoginInfo (userID: String, loginInfoId: Long)


case class Department(id: Option[Int], code:String, name: String, description: String)

case class Project(id: Option[Int], departmentId: Int, code:String, name: String, description: String)

case class Team(id: Option[Int], projectId: Int, code:String, name: String,  description: String)

case class Employee(userId: String, teamId: Int, role:String)


