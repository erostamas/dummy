timestamps {
    node {
        currentBuild.result = "SUCCESS"

        try {
            stage ('Configuration') {
                properties([
                    buildDiscarder(
                        logRotator(
                            numToKeepStr: '10'
                        )
                    ),
                    disableConcurrentBuilds()
                ])
            }

            stage ('Environment variables') {
                echo sh(returnStdout: true, script: 'env')
            }

            stage ('Checkout') {
                checkout scm
            }

            stage ('Generate Jobs') {
                jobDsl(
                    removedJobAction: 'DELETE',
                    removedViewAction: 'DELETE',
                    targets: 'ci/jobs/**/*.groovy, ci/jobs/view.groovy'
                )
            }
        } catch (err) {
            currentBuild.result = "FAILURE"
            throw err
        }
    }
}
