# PA1: Bloom Filter Implementation and Report

To run the `FalsePositives` program, run the following command from the root of
the project:

    ./gradlew -q clean :impl:executeFalsePositives

This command uses the gradle wrapper command packaged with this repository to
clean the project and to execute the `me.dwtj.coms535.pa1.FalsePositive` Java
program. (The `-q` option just suppresses output coming from Gradle itself.)
On my system, it takes just over 30 seconds before the first results are printed
to `STDOUT`.

The database files can be downloaded from the course website and verified
against their expected SHA1 hashes using

    ./gradlew :impl:verifyAllFiles

The `EmpiricalComparison` program can be run using

    .gradlew -q :impl:executeEmpiricalComparison
