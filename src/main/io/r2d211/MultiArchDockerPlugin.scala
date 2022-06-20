package io.r2d211

import com.typesafe.sbt.packager.Keys.dockerAliases
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.Docker
import sbt.Keys._
import sbt._


object MultiArchDockerPlugin extends sbt.AutoPlugin {

  lazy val ensureDockerBuildx = taskKey[Unit]("Ensure that docker buildx configuration exists")
  lazy val dockerBuildWithBuildx = taskKey[Unit]("Build docker images using buildx")
  lazy val dockerBuildxSettings = Seq(
    ensureDockerBuildx := {
      if (Process("docker buildx inspect multi-arch-builder").! == 1) {
        Process("docker buildx create --use --name multi-arch-builder", Keys.baseDirectory.value).!
      }
    },
    dockerBuildWithBuildx := {
      streams.value.log("Building and pushing image with Buildx")
      dockerAliases.value.foreach(
        alias => Process("""docker buildx build --platform=linux/arm64,linux/amd64 --push -t """ +
          alias + " .", baseDirectory.value / "target" / "docker"/ "stage").!
      )
    },
    Docker / publish := Def.sequential(
      Docker / publishLocal,
      ensureDockerBuildx,
      dockerBuildWithBuildx
    ).value
  )

  override def trigger = allRequirements

}
