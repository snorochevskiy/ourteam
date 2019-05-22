ourteam
=======

(just playing with Play2 Framework and Slick)

Project is based on play-scala-seed.g8
```
sbt new playframework/play-scala-seed.g8
```

Most of solutions are taken from:
* https://github.com/mohiva/play-silhouette-seed
* https://github.com/sbrunk/play-silhouette-slick-seed

Running
-------

Prerequireties:
* SBT
* JDK 8+

To run application in development mode, execute:
```
sbt run
```

Other
-----

To get a bcrypt256 password hash:
1) Navigate to project directory:
    cd $project_dir
2) Execute
    sbt console
3) Create hasher and calculate hash
```
    val h = new com.mohiva.play.silhouette.password.BCryptSha256PasswordHasher
    h.hash("1111")
```