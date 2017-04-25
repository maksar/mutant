package SuperProject.buildTypes

import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.BuildFeature
import jetbrains.buildServer.configs.kotlin.v10.BuildFeature.*
import jetbrains.buildServer.configs.kotlin.v10.BuildStep
import jetbrains.buildServer.configs.kotlin.v10.BuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.RetryBuildTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.RetryBuildTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.retryBuild
import jetbrains.buildServer.configs.kotlin.v10.triggers.vcs

object SuperProject_Integration : BuildType({
    template(SuperProject.buildTypes.SuperProject_Template)
    uuid = "9502c847-7dc3-46d9-a828-d9fe55d5c795"
    extId = "SuperProject_Integration"
    name = "Integration"

    vcs {
        root(SuperProject.vcsRoots.Vcs_SuperProject_Commit)
    }

    params {
        param("env.TEAMCITY_INTEGRATION", "99%teamcity.agent.ownPort%")
    }

    steps {
        step {
            name = "Integration"
            id = "RUNNER_11"
            type = "rake-runner"
            param("build-file-path", "Rakefile")
            param("ui.rakeRunner.rspec.specoptions", "--tag js:true")
            param("ui.rakeRunner.ruby.interpreter.path", "/usr/local/bin/ruby")
            param("ui.rakeRunner.ruby.use.mode", "path")
        }
        stepsOrder = arrayListOf("RUNNER_1", "RUNNER_6", "RUNNER_2", "RUNNER_3", "RUNNER_7", "RUNNER_4", "RUNNER_11")
    }

    triggers {
        vcs {
            id = "vcsTrigger"
            perCheckinTriggering = true
        }
        retryBuild {
            id = "retryBuildTrigger"
            delaySeconds = 1
        }
    }

    disableSettings("BUILD_EXT_3", "BUILD_EXT_4", "RUNNER_4", "RUNNER_7")
})
