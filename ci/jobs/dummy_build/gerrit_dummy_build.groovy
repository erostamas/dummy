import groovy.json.*

def jobname = "gerrit_dummy_build_${BRANCH_NAME}"

pipelineJob(jobname) {
    description("""
        <b>Generated Job</b>
        <p>Dummy build - Gerrit
    """.stripIndent().trim())
    concurrentBuild(true)
    triggers {
        gerrit {
            project('erostamas/dummy', "${BRANCH_NAME}")
            events {
                patchsetCreated()
            }
            configure { gerrit ->
                gerrit / serverName('Gerrithub')
                gerrit / 'triggerOnEvents' /
                    'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.events.PluginCommentAddedContainsEvent' /
                        'commentAddedCommentContains'('.*rebuild.*')
                gerrit / 'triggerOnEvents' /
                    'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.events.PluginPatchsetCreatedEvent' /
                        'excludeDrafts'(true)
                gerrit / 'gerritProjects' /
                    'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.GerritProject' << {
                        'filePaths' {
                            'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.FilePath' {
                                'pattern'('*/**')
                                'compareType'('ANT')
                            }
                        }
                        'forbiddenFilePaths' {
                            'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.data.FilePath' {
                                'pattern'('README.md')
                                'compareType'('ANT')
                            }
                        }
                    }
            }
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote{
                        url("ssh://erostamas@review.gerrithub.io:29418/erostamas/dummy")
                        refspec('$GERRIT_REFSPEC')
                        credentials('d15a0909-bacf-4de1-a358-0768d2cf8b33')
                    }
                    branch("${BRANCH_NAME}")
                    extensions{
                        choosingStrategy {
                            gerritTrigger()
                        }
                        cleanBeforeCheckout()
                        relativeTargetDirectory('dummy')
                    }
                }
            }
        }
        scriptPath('ci/jobs/dummy_build/gerrit.Jenkinsfile')
    }
    parameters {
        stringParam('GERRIT_REFSPEC', "refs/heads/${BRANCH_NAME}", '"Build Now" workaround')
    }
}
