package SuperProject.vcsRoots

import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.vcs.GitVcsRoot

object Vcs_SuperProject_Commit : GitVcsRoot({
    uuid = "7473691b-7615-4905-b335-1aae04af09ca"
    extId = "SuperProject_Commit"
    name = "Commit"
    url = "git@github.com:maksar/mutant.git"
    branchSpec = "+:refs/heads/*"
    agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
    authMethod = uploadedKey {
        uploadedKey = "SuperProject_CI"
    }
})
