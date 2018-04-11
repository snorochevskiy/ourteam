package dao

import model.{Company, Department}
import slick.jdbc.JdbcProfile

trait OrganizationTables {

  protected val driver: JdbcProfile
  import driver.api._

  class CompanyTable(tag: Tag) extends Table[Company](tag, "COMPANY") {
    def id = column[String]("ID", O.PrimaryKey)
    def name = column[String]("NAME")
    def * = (id, name) <> (Company.tupled, Company.unapply)
  }
  val companies = TableQuery[CompanyTable]

  // TODO: Move to DAO class
  // def all(): Future[Seq[Company]] = db.run(companies.result)

  class DepartmentTable(tag: Tag) extends Table[Department](tag, "DEPARTMENT"){
    def id = column[String]("ID", O.PrimaryKey)
    def name = column[String]("NAME")
    def companyId = column[String]("COMPANY_ID")
    def * = (id, name, companyId) <> (Department.tupled, Department.unapply)
    def address = foreignKey("COMPANY",companyId,companies)(_.id)
  }
  val departments = TableQuery[DepartmentTable]
}
