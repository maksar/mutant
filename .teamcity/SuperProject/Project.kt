package SuperProject

import SuperProject.buildTypes.*
import SuperProject.vcsRoots.*
import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.Project
import jetbrains.buildServer.configs.kotlin.v10.ProjectFeature
import jetbrains.buildServer.configs.kotlin.v10.ProjectFeature.*
import jetbrains.buildServer.configs.kotlin.v10.projectFeatures.VersionedSettings
import jetbrains.buildServer.configs.kotlin.v10.projectFeatures.VersionedSettings.*
import jetbrains.buildServer.configs.kotlin.v10.projectFeatures.versionedSettings

object Project : Project({
    uuid = "c3299bb0-386e-4a3e-b141-891b77b69dfc"
    extId = "SuperProject"
    parentId = "_Root"
    name = "SuperProject"

    vcsRoot(Vcs_SuperProject_Commit)
    vcsRoot(Vcs_SuperProject_Metrics)

    buildType(SuperProject_Metrics)
    buildType(SuperProject_Staging)
    buildType(SuperProject_Develop)
    buildType(SuperProject_Integration)

    template(SuperProject_Template)

    features {
        versionedSettings {
            id = "PROJECT_EXT_2"
            mode = VersionedSettings.Mode.ENABLED
            buildSettingsMode = VersionedSettings.BuildSettingsMode.PREFER_CURRENT_SETTINGS
            rootExtId = Vcs_SuperProject_Metrics.extId
            showChanges = true
            settingsFormat = VersionedSettings.Format.KOTLIN
        }
        feature {
            id = "PROJECT_EXT_3"
            type = "ReportTab"
            param("startPage", "rubocop.html")
            param("title", "Rubocop")
            param("type", "BuildReportTab")
        }
        feature {
            id = "PROJECT_EXT_4"
            type = "ReportTab"
            param("startPage", "index.html")
            param("title", "Coverage")
            param("type", "BuildReportTab")
        }
        feature {
            id = "PROJECT_EXT_5"
            type = "ReportTab"
            param("startPage", "metric_fu/output/index.html")
            param("title", "Metrics")
            param("type", "BuildReportTab")
        }
    }
})
