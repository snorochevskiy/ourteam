package model

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

case class User(
  id: String, loginInfo: LoginInfo, email: String, firstName:String, lastName: String, middleName: String,
  avatarUrl: String
) extends Identity

case class ContactInfo(phone1: String, phone2: String, phone3: String,
                       hangouts: String, skype: String, telegram: String, viber: String, line: String)



case class Company(id: String, name: String)

case class Department(id: String, name: String, companyId: String)
