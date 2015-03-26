0. Prerequisites

  * Android SDK 1.5 installed
  * android-sdk-mac\_x86-1.5\_r2/tools must be in the system PATH
  * android-sdk-mac\_x86-1.5\_r2/platforms/android-1.5/tools must be in the system path

1. Check out felix4android code

```
svn checkout http://felix4android.googlecode.com/svn/trunk/ felix4android
```

2. Build the basic distribution

```
cd felix4android
mvn clean install -Dpackaging=basic
```

3. Start the emulator

4. Deploy the code

```
cd basic-assembly/target/basic-assembly-1.0.0.dir/basic-assembly-1.0.0/
find * -type f -exec adb push {} /data/felix/{} \;
```

5. Test it!

```
adb shell

# cd data/felix
# ./felix.sh
```

After a few log lines, the familiar Apache Felix shell should appear

```
Welcome to Felix.
=================
```

type
```
-> ps
```

and you should see the list of bundles installed
```
START LEVEL 1
   ID   State         Level  Name
[   0] [Active     ] [    0] System Bundle (1.8.0)
[   1] [Active     ] [    1] Apache Felix Shell Service (1.2.0)
[   2] [Active     ] [    1] Apache Felix Shell TUI (1.2.0)

```
