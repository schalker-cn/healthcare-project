import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:frontend/widgets/base_card.dart';
import 'package:frontend/widgets/custom_app_bar.dart';
import 'package:go_router/go_router.dart';
import 'package:material_symbols_icons/symbols.dart';
import 'package:http/http.dart' as http;

class PatientsScreen extends StatefulWidget {
  const PatientsScreen({
    super.key,
    required this.userId,
  });

  static const String screenName = 'patients';

  final String userId;

  @override
  State<PatientsScreen> createState() => _PatientsScreenState();
}

class _PatientsScreenState extends State<PatientsScreen> {
  List<dynamic> patients = [];

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    fetchPatients();
  }

  void fetchPatients() async {
    var url = getLocalhost(unecodedPath: '/api/getAllPatients');
    print(url);
    var response = await http.get(url);
    print(response.body);
    dynamic result = jsonDecode(response.body);
    dynamic myPatients = [];
    for (var i = 0; i < result.length; i++) {
      if (result[i]['accessToDoctors'].split(",").contains(widget.userId)) {
        myPatients.add(result[i]);
      }
    }
    setState(() {
      // patients = myPatients;
      patients = myPatients;
    });
    print(patients);
  }

  List<Widget> getPatients() {
    List<Widget> getRowDetails(patient) {
      return [
        Expanded(
          child: Text(
            patient['name'],
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
            patient['age'].toString(),
            textAlign: TextAlign.center,
            style: TextStyle(
              color: kDarkColor,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        Text(
          patient['gender'],
          textAlign: TextAlign.center,
          style: TextStyle(
            color: kDarkColor,
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        Expanded(
          child: Text(
            patient['email'],
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
            patient['phone'],
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

    // List<dynamic> patients = [
    //   {
    //     "name": "Alice Bob",
    //     "dateOfBirth": "1990-01-01",
    //     "gender": "F",
    //     "email": "alicebob@gmail.com",
    //     "phoneNo": "+4945223368263"
    //   },
    // ];
    List<Widget> output = [];

    for (var i = 0; i < patients.length; i++) {
      output.add(
        BaseCard(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 32.0),
            child: Row(children: [
              ...getRowDetails(patients[i]),
              Container(
                child: IconButton(
                  icon: Icon(Icons.file_open),
                  color: kPrimaryColor,
                  tooltip: "Go to patient's records",
                  onPressed: () {
                    GoRouter.of(context)
                        .pushNamed('patient-records', queryParameters: {
                      'userType': UserType.doctor.name,
                      'userId': patients[i]['patientID'],
                    });
                  },
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
              title: 'my\npatients',
              icon: Symbols.coronavirus,
            ),
            SizedBox(height: 64),
            ...getPatients(),
          ],
        ),
      ),
    );
  }
}
