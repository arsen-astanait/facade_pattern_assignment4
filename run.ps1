$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
New-Item -ItemType Directory -Path out -Force | Out-Null
$files = @(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName)
javac --release 17 -d out $files
if ($LASTEXITCODE -ne 0) { throw "Compilation failed" }
Write-Host '--- Demo ---'
java -cp out kz.aitu.theater.Main
if ($LASTEXITCODE -ne 0) { throw "Demo failed" }
Write-Host '--- Tests ---'
java -cp out kz.aitu.theater.HomeTheaterFacadeTest
if ($LASTEXITCODE -ne 0) { throw "Tests failed" }
