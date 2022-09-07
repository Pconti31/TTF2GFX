; GUIsliceBuilder Inno Setup SKELETON Script
;
; PLEASE NOTE:
;
; 1. This script is a SKELETON and is meant to be parsed by the Gradle 
;    task "innosetup" before handing it to the Inno Setup compiler (ISCC)
;
; 2. All VARIABLES with a dollar sign and curly brackets are replaced
;    by Gradle, e.g. "applicationVersion" below
;
; 3. The script is COPIED to build/innosetup before its run,
;    so all relative paths refer to this path!
;
; 4. All BACKSLASHES must be escaped 
;

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
ArchitecturesInstallIn64BitMode=x64 ia64
AppId={{778E5FCE-4B56-4C2D-99C5-F27969460457}
AppName=TTF2GFX
AppVersion=${applicationVersion}
AppVerName=TTF2GFX ${applicationVersion}
AppSupportURL=https://github.com/Pconti31/TTF2GFX/issues
AppUpdatesURL=https://github.com/Pconti31/TTF2GFX/releases
DefaultGroupName=TTF2GFX
DefaultDirName={userdocs}\\TTF2GFX
DisableDirPage=no
DisableWelcomePage=no
DisableProgramGroupPage=yes
LicenseFile=..\\..\\LICENSE.txt
OutputDir=.
OutputBaseFilename=ttf2gfx-win-${applicationVersion}
SetupIconFile=..\\tmp\\windows\\TTF2GFX\\ttf2gfx.ico
Compression=lzma
SolidCompression=yes
PrivilegesRequired=none

[Setup]
; Tell Windows Explorer to reload the environment
ChangesEnvironment=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Dirs]
Name: "{app}"; 
Name: "{app}\\logs"; Permissions: everyone-full

[Files]
Source: "..\\tmp\\windows\\TTF2GFX\\ttf2gfx.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\\tmp\\windows\\TTF2GFX\\ttf2gfx.ico"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\\tmp\\windows\\TTF2GFX\\release"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\\tmp\\windows\\TTF2GFX\\bin\\*"; DestDir: "{app}\\bin"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "..\\tmp\\windows\\TTF2GFX\\conf\\*"; DestDir: "{app}\\conf"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "..\\tmp\\windows\\TTF2GFX\\legal\\*"; DestDir: "{app}\\legal"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "..\\tmp\\windows\\TTF2GFX\\lib\\*"; DestDir: "{app}\\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\\TTF2GFX"; Filename: "{app}\\ttf2gfx.bat"
Name: "{commondesktop}\\TTF2GFX"; Filename: "{app}\\ttf2gfx.bat"; IconFilename: "{app}\\ttf2gfx.ico"; Tasks: desktopicon

[Run]
Filename: "{app}\\ttf2gfx.bat"; Description: "{cm:LaunchProgram,TTF2GFX}"; Flags: shellexec postinstall skipifsilent

[Code]

function IsRegularUser(): Boolean;
begin
Result := not (IsAdminLoggedOn or IsPowerUserLoggedOn);
end;

