package SuperProject.buildTypes

import SuperProject.vcsRoots.Vcs_SuperProject_Metrics
import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.buildFeatures.CommitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v10.buildFeatures.CommitStatusPublisher.*
import jetbrains.buildServer.configs.kotlin.v10.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v10.triggers.ScheduleTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.ScheduleTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.schedule

object SuperProject_Metrics : BuildType({
    template(SuperProject.buildTypes.SuperProject_Template)
    uuid = "989e0f1c-01f1-4a86-bdd3-ab7e62dfc351"
    extId = "SuperProject_Metrics"
    name = "Metrics"

    artifactRules = """
        coverage/
        tmp/
    """.trimIndent()
    maxRunningBuilds = 1

    vcs {
        root(SuperProject.vcsRoots.Vcs_SuperProject_Metrics)
    }

    steps {
        script {
            name = "Metric_fu"
            id = "RUNNER_10"
            scriptContent = "bundle exec metric_fu --no-open"
        }
        stepsOrder = arrayListOf("RUNNER_1", "RUNNER_6", "RUNNER_2", "RUNNER_3", "RUNNER_7", "RUNNER_4", "RUNNER_10")
    }

    triggers {
        schedule {
            id = "TRIGGER_2"
            schedulingPolicy = daily {
                hour = 1
            }
            triggerBuild = always()
            param("revisionRule", "lastFinished")
            param("triggerBuildWithPendingChangesOnly", "")
            param("dayOfWeek", "Sunday")
        }
    }

    dependencies {
        artifacts("SuperProject_Metrics") {
            id = "ARTIFACT_DEPENDENCY_1"
            buildRule = lastFinished()
            cleanDestination = true
            artifactRules = "metric_fu/**=>tmp/metric_fu"
            // Uncomment this when Metrics build will be finished without errors.
            enabled = false
        }
    }

    features {
        commitStatusPublisher {
            id = "BUILD_EXT_11"
            vcsRootExtId = Vcs_SuperProject_Metrics.extId
                        publisher = github {
                githubUrl = "https://api.github.com"
                authType = password {
                    userName = ""
                    password = ""
                }
            }
        }
    }

    disableSettings("BUILD_EXT_1")
})
