$searchPath = "d:\laragon\www\FoodTrack\src\main\java\com\foodtrack"
$javaFiles = Get-ChildItem -Path $searchPath -Filter *.java -Recurse

foreach ($file in $javaFiles) {
    if ($file.Name -contains "FoodTrackApplication.java") { continue }
    if ($file.Name -contains "SecurityConfig.java") { continue }
    if ($file.Name -contains "AdminWebController.java") { continue }

    $lines = Get-Content $file.FullName -Raw
    
    # Skip if it already has a class level javadoc at the top near the class declaration
    # A simple reliable check is whether "/**" exists 
    if ($lines -match "\/\*\*") {
        # Already has some doc, we can partially skip or add. Actually, let's just add it if it doesn't exist
        # wait, let me just add it before 'public class' or 'public interface'
    }

    $folder = $file.Directory.Name
    $className = $file.BaseName
    
    $comment = ""
    switch ($folder) {
        "entity" {
            $comment = "`r`n`r`n/**`r`n * Entitas atau model data untuk $className.`r`n * Digunakan untuk merepresentasikan struktur tabel di database.`r`n */`r`n"
        }
        "repository" {
            $comment = "`r`n`r`n/**`r`n * Repository interface untuk mengelola operasi database (CRUD)`r`n * pada data $className.`r`n */`r`n"
        }
        "service" {
            $comment = "`r`n`r`n/**`r`n * Kelas Service untuk $className.`r`n * Berisi logika bisnis dan bertindak sebagai penghubung antara Controller dan Repository.`r`n */`r`n"
        }
        "web" {
            $comment = "`r`n`r`n/**`r`n * Web Controller untuk $className.`r`n * Menangani request HTTP (GET/POST) dan mengatur respons antarmuka pengguna (View).`r`n */`r`n"
        }
        "config" {
            $comment = "`r`n`r`n/**`r`n * Kelas konfigurasi untuk $className di dalam spring boot.`r`n */`r`n"
        }
        default {
            $comment = "`r`n`r`n/**`r`n * Kelas $className.`r`n */`r`n"
        }
    }

    # Only insert if no /** comment is found just before public class/interface/enum
    if ($lines -notmatch "\/\*\*.*\r?\n.*public (class|interface|enum) $className") {
        # Replace 'public class/interface/enum className' with comment + original
        
        $lines = $lines -replace "(?m)^(public\s+(?:class|interface|enum|abstract\s+class)\s+$className\b)", "$comment`$1"
        
        # If it's a repository that just extends, it might be public interface.
        Set-Content -Path $file.FullName -Value $lines -Encoding UTF8
    }
}
Write-Host "Komentar berhasil ditambahkan ke semua file .java"
