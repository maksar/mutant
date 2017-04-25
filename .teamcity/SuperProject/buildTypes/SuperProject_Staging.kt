package SuperProject.buildTypes

import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.vcs

object SuperProject_Staging : BuildType({
    template(SuperProject.buildTypes.SuperProject_Template)
    uuid = "381e441f-a06b-418c-a6d2-9be882ea4776"
    extId = "SuperProject_Staging"
    name = "Staging"

    vcs {
        root(SuperProject.vcsRoots.Vcs_SuperProject_Metrics)
    }

    steps {
        script {
            name = "Deploy"
            id = "RUNNER_5"
            scriptContent = "RAILS_ENV=development bundle exec cap staging deploy"
        }
        stepsOrder = arrayListOf("RUNNER_1", "RUNNER_6", "RUNNER_2", "RUNNER_3", "RUNNER_7", "RUNNER_4", "RUNNER_5")
    }

    triggers {
        vcs {
            id = "vcsTrigger"
            triggerRules = "+:*"
        }
    }

    disableSettings("BUILD_EXT_1")
})
