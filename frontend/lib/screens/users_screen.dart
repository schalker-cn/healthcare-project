import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:frontend/models/doctor.dart';
import 'package:frontend/models/hospital.dart';
import 'package:frontend/models/patient.dart';
import 'package:frontend/widgets/base_card.dart';
import 'package:frontend/widgets/custom_app_bar.dart';
import 'package:material_symbols_icons/symbols.dart';

class UsersScreen extends StatefulWidget {
  const UsersScreen({super.key});

  static const String screenName = 'users';

  @override
  State<UsersScreen> createState() => _UsersScreenState();
}

class _UsersScreenState extends State<UsersScreen> {
  List<Widget> getRecords() {
    List<Widget> getRowDetails(user) {
      return [
        Expanded(
          child: Text(
            user is Patient ? 'Patient' : 'Doctor',
            textAlign: TextAlign.center,
            style: TextStyle(
              color: kDarkColor.withOpacity(.5),
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        Expanded(
          child: Text(
            user.name,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: kDarkColor,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        Text(
          user.gender.toString().split('.').last[0].toUpperCase(),
          textAlign: TextAlign.center,
          style: TextStyle(
            color: kDarkColor,
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        Expanded(
          child: Text(
            user.email,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: kDarkColor,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        Expanded(
          child: Text(
            user.phone,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: kDarkColor,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        Expanded(
          child: Text(
            user is Patient ? user.dateOfBirth : user.speciality,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: kDarkColor,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ];
    }

    List<dynamic> users = [
      Patient(
        patientId: '123',
        hospitalId: '123',
        name: 'Alice Bob',
        dateOfBirth: '1993-01-01',
        gender: Gender.female,
        email: 'alice@gmail.com',
        phone: '+4933223134532',
      ),
      Doctor(
          doctorId: '432',
          name: "Dr. Jane Doe",
          dateOfBirth: '1994-01-01',
          gender: Gender.male,
          email: 'jane@gmail.com',
          phone: '+4934342313453',
          address: 'XYZStrasse 6, 82324 Munich',
          speciality: 'Neurosurgeon',
          hospitalId: '423'),
    ];

    List<Widget> output = [];

    for (var i = 0; i < users.length; i++) {
      output.add(
        BaseCard(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 32.0),
            child: Row(children: [
              ...getRowDetails(users[i]),
              Container(
                child: IconButton(
                  icon: Icon(Icons.cancel_outlined),
                  color: Colors.red,
                  tooltip: "Remove user",
                  onPressed: () {},
                ),
              )
            ]),
          ),
        ),
      );
      output.add(SizedBox(height: 16));
    }
    return output;
  }

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Scaffold(
      body: Container(
        width: double.infinity,
        height: double.infinity,
        decoration: BoxDecoration(
          color: kDarkColor,
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            CustomAppBar(
              title: 'all\nusers',
              icon: Symbols.group,
            ),
            SizedBox(height: 64),
            ...getRecords(),
            SizedBox(height: 64),
            Container(
              width: size.width * .6,
              child: ElevatedButton(
                onPressed: () {},
                child: Text(
                  "Add new user",
                  style: TextStyle(
                    fontSize: 24,
                    color: Colors.white,
                  ),
                ),
                style: ButtonStyle(
                  backgroundColor: WidgetStateProperty.all(kPrimaryColor),
                  padding: WidgetStateProperty.all(
                      EdgeInsets.symmetric(vertical: 16, horizontal: 32)),
                  shape: WidgetStateProperty.all(RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(0),
                  )),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
