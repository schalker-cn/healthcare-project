import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:frontend/screens/home_screen.dart';
import 'package:frontend/widgets/nav_card.dart';
import 'package:frontend/widgets/user_type_card.dart';

class LandingScreen extends StatelessWidget {
  const LandingScreen({super.key});

  static const String screenName = 'landing';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        width: double.infinity,
        height: double.infinity,
        decoration: BoxDecoration(
          color: kDarkColor,
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Spacer(),
            // NavCard(
            //   path: HomeScreen.screenName,
            //   icon: Icons.home,
            //   name: 'patient\ndashboard',
            //   queryParameters: {'userType': UserType.patient.name},
            // ),
            UserTypeCards(),
            Spacer(),
          ],
        ),
      ),
    );
  }
}
