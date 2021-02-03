export GPG_TTY=$(tty)
mvn versions:set -DnewVersion=1.1.3 -DgenerateBackupPoms=false
mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} versions:commit

mvn versions:commit
mvn clean deploy -Ppro
