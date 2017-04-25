package SuperProject.buildTypes


import SuperProject.vcsRoots.Vcs_SuperProject_Commit
import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.BuildStep
import jetbrains.buildServer.configs.kotlin.v10.BuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildFeatures.CommitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v10.buildFeatures.CommitStatusPublisher.*
import jetbrains.buildServer.configs.kotlin.v10.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ExecBuildStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ExecBuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.ScriptBuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v10.failureConditions.BuildFailureOnMetric
import jetbrains.buildServer.configs.kotlin.v10.failureConditions.BuildFailureOnMetric.*
import jetbrains.buildServer.configs.kotlin.v10.failureConditions.failOnMetricChange

object SuperProject_Template : Template({
    uuid = "596f85c4-de6d-4266-8296-22ac912d508e"
    extId = "SuperProject_Template"
    name = "Template"

    enablePersonalBuilds = false
    artifactRules = """
        coverage/
        tmp/capybara/
    """.trimIndent()

    vcs {
        checkoutMode = CheckoutMode.ON_AGENT
    }

    steps {
        script {
            name = "Bundle Install"
            id = "RUNNER_1"
            scriptContent = "bundle install --path ~/.gems"
        }
        script {
            name = "Secrets config"
            id = "RUNNER_6"
            scriptContent = """
                bundle exec tamplier copy
                echo 'test:
                  secret_key_base: 60b5883572746c0014e9f8631ebada54df969dbd3cd78ae13e370d104733c48b06182fac13b8c758d62eb44adbd3b6fef029fc897a8d085b24fee9eabe3968e3
                ' > %teamcity.build.checkoutDir%/config/secrets.yml
            """.trimIndent()
        }
        script {
            name = "DB Config"
            id = "RUNNER_2"
            scriptContent = """
                echo 'test:
                  adapter: postgresql
                  encoding: unicode
                  reconnect: true
                  host: postgres
                  pool: 5
                  database: SuperProject_test_%teamcity.agent.name%
                  username: postgres
                  password: postgres
                ' > %teamcity.build.checkoutDir%/config/database.yml
            """.trimIndent()
        }
        script {
            name = "DB prepare"
            id = "RUNNER_3"
            scriptContent = "RAILS_ENV=test bundle exec rake db:drop db:create db:migrate"
        }
        exec {
            name = "Rubocop"
            id = "RUNNER_7"
            path = "bundle"
            arguments = "exec bin/rubocop"
        }
        step {
            name = "Rspec"
            id = "RUNNER_4"
            type = "rake-runner"
            param("build-file-path", "Rakefile")
            param("ui.rakeRunner.ruby.interpreter.path", "/usr/local/bin/ruby")
            param("ui.rakeRunner.ruby.use.mode", "path")
        }
    }

    features {
        commitStatusPublisher {
            id = "BUILD_EXT_1"
            vcsRootExtId = Vcs_SuperProject_Commit.extId
                        publisher = github {
                githubUrl = "https://api.github.com"
                authType = password {
                    userName = ""
                    password = ""
                }
            }
        }

        feature {
            id = "BUILD_EXT_2"
            type = "xml-report-plugin"
            param("xmlReportParsing.reportDirs", "coverage/rubocop.xml")
            param("xmlReportParsing.reportType", "checkstyle")
        }
    }


    failureConditions {
        failOnMetricChange {
            id = "BUILD_EXT_3"
            metric = BuildFailureOnMetric.MetricType.INSPECTION_ERROR_COUNT
            threshold = 0
            units = BuildFailureOnMetric.MetricUnit.DEFAULT_UNIT
            comparison = BuildFailureOnMetric.MetricComparison.MORE
            param("anchorBuild", "lastSuccessful")
        }
        failOnMetricChange {
            id = "BUILD_EXT_4"
            metric = BuildFailureOnMetric.MetricType.INSPECTION_WARN_COUNT
            threshold = 0
            units = BuildFailureOnMetric.MetricUnit.DEFAULT_UNIT
            comparison = BuildFailureOnMetric.MetricComparison.MORE
            param("anchorBuild", "lastSuccessful")
        }
    }
})
