# Galaxy Force


## Gradle Properties

***

### Configuring Gradle Properties

The gradle build file (`app/build.gradle`) requires a number of secret properties to allow the application to successfully build.

These secrets should be kept separate from the application and never saved to source control. This is achieved by storing these properties in the user's home directory under: `/.gradle/gradle.properties`.

The properties below are used to provide the application's public key. The key is reversed and then split over the 4 separate public key properties.
```
PUBLIC_KEY1
PUBLIC_KEY2
PUBLIC_KEY3
PUBLIC_KEY4
```

The properties below allow the user to build a signed APK using the application's keystore:
```
KEYSTORE_FILE
KEYSTORE_PASSWORD
KEY_ALIAS
KEY_PASSWORD
```

Once the correct values are provided for each property in the above `/.gradle/gradle.properties` file, the application can be built successfully.

***


## Deploying to the Android Play Console

***

### Building a Signed APK

From Android Studio, create a new signed APK.

```
Build -> Clean Project
```

```
Build -> Generate Signed Bundle / APK...
```

Choose APK then click `Next`.

![Create APK Image](https://github.com/DannyNicholas/galaxyforce/raw/dev/assets/createAPK.png "Create APK Image")

Enter details key store details then click `Next`. It is important that the same keystore is used every time as otherwise new APKs will be rejected by Google Play Console.

Choose `release` build variant and the two signature versions. Click `Finish`.

![Choose Build Variant](https://github.com/DannyNicholas/galaxyforce/raw/dev/assets/buildVariant.png "Choose Build Variant")

***

### Testing a Signed APK

The signed APK can be tested on a real Android device by installing it onto a real device.

Find path to the Android SDK platform tools. Normally...
```
<path-to-android-sdk-tools>/android-sdk-macosx/platform-tools
```

Confirm Android device is connected:
```
./adb devices
```

Uninstall previous versions of the app:
```
./adb uninstall com.danosoftware.galaxyforce
```

Install the signed APK:
```
./adb install <path-to-galaxyforce-project>/app/release/app-release.apk
```

***

### Deploy to Google Play Console

Click on `Release Management -> App Releases`. Currently these are deployed to a Beta closed track. Click on `Manage`.

![App Release](https://github.com/DannyNicholas/galaxyforce/raw/dev/assets/appRelease.png "App Release")

Here, you can create releases and manage beta testers.

Click `Release` to create a new release.

Here you can:

- Upload your signed APK (`.../app/release/app-release.apk`)
- Enter a release name
- Add release notes

Then press `Save`.

![Upload](https://github.com/DannyNicholas/galaxyforce/raw/dev/assets/upload.png "Upload")

Click `Review` to see the release changes.
Once done, click `Start Rollout to Beta`.

***

### Upload Deobfuscation Files

To aid investigation of crash reports, the deobfuscation mapping files must be uploaded. These mapping files change with every APK so must be re-uploaded with every new APK.

Click on
```Android Vitals -> Deobsfucation files```

Upload all the mapping files for the current APK (`.../app/build/outputs/mapping/release/`)

![Deobsfucation Mapping Files](https://github.com/DannyNicholas/galaxyforce/raw/dev/assets/deobsfucation.png "Deobsfucation Mapping Files")
