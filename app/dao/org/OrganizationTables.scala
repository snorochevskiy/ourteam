package dao.org

import model._
import slick.jdbc.JdbcProfile

trait OrganizationTables {

  protected val driver: JdbcProfile
  import driver.api._

  @deprecated
  class CompanyTable(tag: Tag) extends Table[Company](tag, "COMPANY") {
    def id = column[String]("ID", O.PrimaryKey)
    def name = column[String]("NAME")
    def * = (id, name) <> (Company.tupled, Company.unapply)
  }
  val companies = TableQuery[CompanyTable]


  class DepartmentTable(tag: Tag) extends Table[Department](tag, "DEPARTMENT"){
    def id = column[String]("ID", O.PrimaryKey)
    def name = column[String]("NAME")
    def description = column[String]("DESCRIPTION")
    def * = (id, name, description) <> (Department.tupled, Department.unapply)
  }
  val departments = TableQuery[DepartmentTable]


  class ProjectTable(tag: Tag) extends Table[Project](tag, "PROJECT"){
    def id = column[String]("ID", O.PrimaryKey)
    def departmentId = column[String]("DEPARTMENT_ID")
    def name = column[String]("NAME")
    def description = column[String]("DESCRIPTION")

    def * = (id, departmentId, name, description) <> (Project.tupled, Project.unapply)
    def departmentFk = foreignKey("DEPARTMENT_FK", departmentId, departments)(_.id)
  }
  val projects = TableQuery[ProjectTable]


  class TeamTable(tag: Tag) extends Table[Team](tag, "TEAM") {
    def id = column[String]("ID", O.PrimaryKey)
    def projectId = column[String]("PROJECT_ID")
    def name = column[String]("NAME")
    def description = column[String]("DESCRIPTION")

    override def * = (id, projectId, name, description) <> (Team.tupled, Team.unapply)
    def projectFk = foreignKey("PROJECT_FK", projectId, projects)(_.id)
  }
  val teams = TableQuery[TeamTable]


  class EmployeeTable(tag: Tag) extends Table[Employee](tag, "EMPLOYEE") {
    def userId = column[String]("USER_ID")
    def teamId = column[String]("TEAM_ID")
    def role = column[String]("FUNCTIONAL_ROLE")

    // TODO: how to add FK to Users ? Maybe put all mapping into a single file?
    override def * = (userId, teamId, role) <> (Employee.tupled, Employee.unapply)
    def teamFk = foreignKey("TEAM_FK", teamId, teams)(_.id)
  }
  val employees = TableQuery[EmployeeTable]

}
