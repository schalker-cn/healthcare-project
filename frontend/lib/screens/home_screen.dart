import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:frontend/widgets/nav_card.dart';
import 'package:go_router/go_router.dart';
import 'package:material_symbols_icons/symbols.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({
    super.key,
    required this.userType,
    required this.userId,
  });

  static const String screenName = 'dashboard';

  final UserType userType;
  final String userId;

  List<Widget> getPatientDashboard() {
    return [
      Spacer(),
      NavCard(
        icon: Symbols.stethoscope,
        path: 'doctors',
        name: 'my\ndoctors',
        queryParameters: {'userId': userId},
      ),
      Spacer(),
      NavCard(
        icon: Symbols.note_add,
        path: 'records',
        name: 'my\nrecords',
        queryParameters: {'userType': UserType.patient.name, 'userId': userId},
      ),
      Spacer(),
      NavCard(
        icon: Symbols.person,
        path: 'profile',
        name: 'my\nprofile',
        queryParameters: {'userType': UserType.patient.name, 'userId': userId},
      ),
      Spacer(),
    ];
  }

  List<Widget> getDoctorDashboard() {
    return [
      Spacer(),
      NavCard(
        icon: Symbols.coronavirus,
        path: 'patients',
        name: 'my\npatients',
        queryParameters: {'userId': userId},
      ),
      Spacer(),
      NavCard(
        icon: Symbols.person,
        path: 'profile',
        name: 'my\nprofile',
        queryParameters: {'userType': UserType.doctor.name, 'userId': userId},
      ),
      Spacer(),
    ];
  }

  List<Widget> getHospitalDashboard() {
    return [
      Spacer(),
      NavCard(
        icon: Symbols.group,
        path: 'users',
        name: 'all\nusers',
      ),
      Spacer(),
      NavCard(
        icon: Symbols.pill,
        path: 'medicines',
        name: 'all\nmedicines',
        queryParameters: {'userType': UserType.hospital.name, 'userId': userId},
      ),
      Spacer(),
      NavCard(
        icon: Symbols.person,
        path: 'profile',
        name: 'my\nprofile',
        queryParameters: {'userType': UserType.hospital.name, 'userId': userId},
      ),
      Spacer(),
    ];
  }

  List<Widget> getProducerDashboard() {
    return [
      Spacer(),
      NavCard(
        icon: Symbols.pill,
        path: 'medicines',
        name: 'all\nmedicines',
        queryParameters: {'userType': UserType.producer.name, 'userId': userId},
      ),
      Spacer(),
      NavCard(
        icon: Symbols.person,
        path: 'profile',
        name: 'my\nprofile',
        queryParameters: {'userType': UserType.producer.name, 'userId': userId},
      ),
      Spacer(),
    ];
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        width: double.infinity,
        height: double.infinity,
        decoration: BoxDecoration(
          color: kDarkColor,
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: userType == UserType.patient
                  ? getPatientDashboard()
                  : userType == UserType.doctor
                      ? getDoctorDashboard()
                      : userType == UserType.hospital
                          ? getHospitalDashboard()
                          : userType == UserType.producer
                              ? getProducerDashboard()
                              : [],
            ),
            SizedBox(height: 64),
            IconButton(
              onPressed: () {
                context.pop();
              },
              icon: Icon(
                Icons.home,
                color: kPrimaryColor,
                size: 48,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
