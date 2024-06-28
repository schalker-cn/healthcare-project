import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'go_router_config.dart';
import 'package:url_strategy/url_strategy.dart';

void main() {
  setPathUrlStrategy();
  GoRouter.optionURLReflectsImperativeAPIs = true;
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      routerConfig: router2,
      debugShowCheckedModeBanner: false,
      title: 'EHR App',
    );
  }
}
