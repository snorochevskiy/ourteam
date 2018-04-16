package dao.org

import model._
import slick.jdbc.JdbcProfile

trait OrganizationTables {

  protected val driver: JdbcProfile
  import driver.api._

  class DepartmentTable(tag: Tag) extends Table[Department](tag, "DEPARTMENT"){
    def id = column[Int]("ID", O.PrimaryKey)
    def code = column[String]("CODE")
    def name = column[String]("NAME")
    def description = column[String]("DESCRIPTION")
    def * = (id, code, name, description) <> (Department.tupled, Department.unapply)
  }
  val departments = TableQuery[DepartmentTable]


  class ProjectTable(tag: Tag) extends Table[Project](tag, "PROJECT"){
    def id = column[Int]("ID", O.PrimaryKey)
    def departmentId = column[Int]("DEPARTMENT_ID")
    def code = column[String]("CODE")
    def name = column[String]("NAME")
    def description = column[String]("DESCRIPTION")

    def * = (id, departmentId, code, name, description) <> (Project.tupled, Project.unapply)
    def departmentFk = foreignKey("DEPARTMENT_FK", departmentId, departments)(_.id)
  }
  val projects = TableQuery[ProjectTable]


  class TeamTable(tag: Tag) extends Table[Team](tag, "TEAM") {
    def id = column[Int]("ID", O.PrimaryKey)
    def projectId = column[Int]("PROJECT_ID")
    def code = column[String]("CODE")
    def name = column[String]("NAME")
    def description = column[String]("DESCRIPTION")

    override def * = (id, projectId, code, name, description) <> (Team.tupled, Team.unapply)
    def projectFk = foreignKey("PROJECT_FK", projectId, projects)(_.id)
  }
  val teams = TableQuery[TeamTable]


  class EmployeeTable(tag: Tag) extends Table[Employee](tag, "EMPLOYEE") {
    def userId = column[String]("USER_ID")
    def teamId = column[Int]("TEAM_ID")
    def role = column[String]("FUNCTIONAL_ROLE")

    // TODO: how to add FK to Users ? Maybe put all mapping into a single file?
    override def * = (userId, teamId, role) <> (Employee.tupled, Employee.unapply)
    def teamFk = foreignKey("TEAM_FK", teamId, teams)(_.id)
  }
  val employees = TableQuery[EmployeeTable]

}
