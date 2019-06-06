name := """ourteam"""
organization := "org.ourteam"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

resolvers := ("Atlassian Releases" at "https://maven.atlassian.com/public/") +: resolvers.value

libraryDependencies += guice
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.4"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"

libraryDependencies += "javax.xml.bind" % "jaxb-api" % "2.3.1"

libraryDependencies += "com.h2database" % "h2" % "1.4.196"
//libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.11"

libraryDependencies += "org.webjars" %% "webjars-play" % "2.7.0-1"
libraryDependencies += "org.webjars" % "bootstrap" % "4.0.0-2"

libraryDependencies += "com.mohiva" %% "play-silhouette" % "6.0.0-RC1"
libraryDependencies += "com.mohiva" %% "play-silhouette-password-bcrypt" % "6.0.0-RC1"
libraryDependencies += "com.mohiva" %% "play-silhouette-persistence" % "6.0.0-RC1"
libraryDependencies += "com.mohiva" %% "play-silhouette-crypto-jca" % "6.0.0-RC1"

libraryDependencies += "com.iheart" %% "ficus" % "1.4.6"

//libraryDependencies += "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.1-akka-2.5.x"
libraryDependencies += "com.enragedginger" %% "akka-quartz-scheduler" % "1.8.0-akka-2.5.x"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.ourteam.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.ourteam.binders._"
