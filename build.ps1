$ErrorActionPreference = "Stop"

if (-not $env:JAVA_HOME) {
    $knownJava = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
    if (Test-Path "$knownJava\bin\java.exe") {
        $env:JAVA_HOME = $knownJava
        $env:Path = "$env:JAVA_HOME\bin;$env:Path"
        Write-Host "Using Java at $env:JAVA_HOME"
    }
}

if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    throw "Java 21 was not found. Set JAVA_HOME before running this script."
}

if (-not (Test-Path ".\libs\Cobblemon-neoforge-1.7.3+1.21.1.jar")) {
    throw "Copy Cobblemon-neoforge-1.7.3+1.21.1.jar into the libs folder first."
}

.\gradlew clean build --no-build-cache
