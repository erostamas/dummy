import groovy.json.*

def jobname = "git_dummy_build_${BRANCH_NAME}"

pipelineJob(jobname) {
    description("""
        <b>Generated Job</b>
        <p>Dummy build
    """.stripIndent().trim())
    concurrentBuild(true)
    triggers {
        gerrit {
            project('erostamas/dummy', "${BRANCH_NAME}")
            events {
                changeMerged()
            }
            configure { gerrit ->
                gerrit / serverName('Gerrithub')
                gerrit / 'triggerOnEvents' /
                    'com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.events.PluginCommentAddedContainsEvent' /
                        'commentAddedCommentContains'('.*rebuild.*')
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
        scriptPath('ci/jobs/dummy_build/git.Jenkinsfile')
    }
    parameters {
        stringParam('GERRIT_REFSPEC', "refs/heads/${BRANCH_NAME}", '"Build Now" workaround')
    }
}
