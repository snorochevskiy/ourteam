package forms

import play.api.data.Form
import play.api.data.Forms.{mapping, text}

object CompanyForms {

  case class CompanyFormData(id: String, name: String)

  val companyForm = Form(
    mapping(
      "id" -> text,
      "name" -> text
    )(CompanyFormData.apply)(CompanyFormData.unapply)
  )

}
