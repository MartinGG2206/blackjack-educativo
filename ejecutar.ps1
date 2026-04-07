$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "Java no esta disponible en PATH." -ForegroundColor Red
    Write-Host "Instala Java 17 o agrega java al PATH del sistema."
    exit 1
}

& "$PSScriptRoot\mvnw.cmd" -q package
if ($LASTEXITCODE -ne 0) {
    Write-Host "No se pudo compilar o empaquetar la aplicacion." -ForegroundColor Red
    exit $LASTEXITCODE
}

Start-Process java -ArgumentList "-jar", "`"$PSScriptRoot\target\blackjack-educativo-1.0.0.jar`""
Start-Sleep -Seconds 4
Start-Process "http://localhost:8080"
