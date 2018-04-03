package dao

import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait Dao extends SlickTables with HasDatabaseConfigProvider[JdbcProfile]
