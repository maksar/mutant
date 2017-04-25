package SuperProject.buildTypes

import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.BuildFeature
import jetbrains.buildServer.configs.kotlin.v10.BuildFeature.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.vcs

object SuperProject_Develop : BuildType({
    template(SuperProject.buildTypes.SuperProject_Template)
    uuid = "69a27b21-c7fb-4754-a43b-a996f460a1be"
    extId = "SuperProject_Develop"
    name = "Develop"

    artifactRules = """
        coverage/
        tmp/capybara/
    """.trimIndent()

    vcs {
        root(SuperProject.vcsRoots.Vcs_SuperProject_Commit)
    }

    triggers {
        vcs {
            id = "vcsTrigger"
            perCheckinTriggering = true
        }
    }
})
