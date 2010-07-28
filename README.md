#IconReader2
This is a development tool to show icons(android.R.drawable.*) on your android-system

## Try it

__Installing android SDK__

See the [official android doc](http://developer.android.com/sdk/installing.html)


__Create emulator__

The command line for creating an AVD has the following syntax:
	android create avd -n <name> -t <targetID> [-<option> <value>] ...

Here's an example that creates an AVD with the name "my_android1.5" and target ID "3":
	android create avd -n my_android1.5 -t 3 

For more information about how to manage AVDs, see [developer.android.com](http://developer.android.com/guide/developing/tools/avd.html)


__Startup emulator__

now, startup your avd using this command:
	emulator -avd my_android1.5


__Run IconReader2__
	cd $IconReader2_HOME
	ant install

