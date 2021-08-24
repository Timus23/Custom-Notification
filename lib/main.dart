
import 'package:call_notification/call_notification.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  CallNotification.showNotification();
}

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  getToken() async {
    final res = await FirebaseMessaging.instance.getToken();
    print(res);
  }

  @override
  void initState() {
    super.initState();
    getToken();

    FirebaseMessaging.onMessage.listen((event) {
      CallNotification.showNotification();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            SizedBox(height: 40),
            TextButton(
              onPressed: () {
                CallNotification.showNotification();
              },
              child: Text("Plugin Notification"),
            ),
            SizedBox(height: 40),
            TextButton(
              onPressed: () {
                try {
                  const platform = MethodChannel('com.timus/notification');
                  platform.invokeMethod("callNotification");
                } on PlatformException catch (e) {
                  print(e.toString());
                }
              },
              child: Text("Notification"),
            ),
            SizedBox(
              height: 40,
            ),
            Builder(builder: (context) {
              return TextButton(
                onPressed: () async {
                  final token = await FirebaseMessaging.instance.getToken();
                  Clipboard.setData(ClipboardData(text: token));
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      content: Text('Firebase Token Copied'),
                    ),
                  );
                },
                child: Text("Copy Firebase Token"),
              );
            })
          ],
        ),
      ),
    );
  }
}
