import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:frontend/widgets/base_card.dart';
import 'package:frontend/widgets/custom_app_bar.dart';
import 'package:material_symbols_icons/symbols.dart';
import 'package:http/http.dart' as http;

class DoctorsScreen extends StatefulWidget {
  const DoctorsScreen({
    super.key,
    required this.userId,
  });

  static const String screenName = 'doctors';

  final String userId;

  @override
  State<DoctorsScreen> createState() => _DoctorsScreenState();
}

class _DoctorsScreenState extends State<DoctorsScreen> {
  int _selectedIndex = 0;

  List<dynamic> doctors = [];
  dynamic patient;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    fetchPatient();
    fetchDoctors();
  }

  void fetchDoctors() async {
    var url = getLocalhost(unecodedPath: 'api/getAllDoctors');
    var response = await http.get(url);
    print(response.body);
    dynamic result = jsonDecode(response.body);
    // dynamic doctors = [];
    // for (var i = 0; i < result.length; i++) {
    //   if (result[i]['type'] == 'doctor') {
    //     doctors.add(result[i]);
    //   }
    // }
    setState(() {
      doctors = result;
    });
  }

  void fetchPatient() async {
    var url = getLocalhost(unecodedPath: 'api/readPatient/${widget.userId}');
    var response = await http.get(url);
    print(response.body);
    dynamic result = jsonDecode(response.body);
    setState(() {
      patient = result;
    });
  }

  Widget getTabs() {
    final size = MediaQuery.of(context).size;
    return Container(
      width: size.width * .6,
      child: Row(
        children: [
          Spacer(),
          Expanded(
            child: MouseRegion(
              cursor: SystemMouseCursors.click,
              child: GestureDetector(
                onTap: () {
                  setState(() {
                    _selectedIndex = 0;
                  });
                },
                child: Container(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  decoration: BoxDecoration(
                    color: _selectedIndex == 0 ? kPrimaryColor : kDarkColor,
                    border: Border(
                      bottom: BorderSide(
                        color: _selectedIndex == 0 ? kDarkColor : kPrimaryColor,
                        width: 2,
                      ),
                      top: BorderSide(
                        color: _selectedIndex == 0 ? kDarkColor : kPrimaryColor,
                        width: 2,
                      ),
                      right: BorderSide(
                        color: _selectedIndex == 0 ? kDarkColor : kPrimaryColor,
                        width: 2,
                      ),
                      left: BorderSide(
                        color: _selectedIndex == 0 ? kDarkColor : kPrimaryColor,
                        width: 2,
                      ),
                    ),
                  ),
                  child: Center(
                    child: Text(
                      'My Doctors',
                      style: TextStyle(
                        color:
                            _selectedIndex == 0 ? kLightColor : kPrimaryColor,
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ),
          Spacer(),
          Expanded(
            child: MouseRegion(
              cursor: SystemMouseCursors.click,
              child: GestureDetector(
                onTap: () {
                  setState(() {
                    _selectedIndex = 1;
                  });
                },
                child: Container(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  decoration: BoxDecoration(
                    color: _selectedIndex == 1 ? kPrimaryColor : kDarkColor,
                    border: Border(
                      bottom: BorderSide(
                        color: _selectedIndex == 1 ? kDarkColor : kPrimaryColor,
                        width: 2,
                      ),
                      top: BorderSide(
                        color: _selectedIndex == 1 ? kDarkColor : kPrimaryColor,
                        width: 2,
                      ),
                      right: BorderSide(
                        color: _selectedIndex == 1 ? kDarkColor : kPrimaryColor,
                        width: 2,
                      ),
                      left: BorderSide(
                        color: _selectedIndex == 1 ? kDarkColor : kPrimaryColor,
                        width: 2,
                      ),
                    ),
                  ),
                  child: Center(
                    child: Text(
                      'Add Doctor',
                      style: TextStyle(
                        color:
                            _selectedIndex == 1 ? kLightColor : kPrimaryColor,
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ),
          Spacer(),
        ],
      ),
    );
  }

  List<Widget> getDoctors() {
    List<Widget> getRowDetails(doctor) {
      return [
        // Spacer(),
        Expanded(
          child: Text(
            doctor['name'],
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
            doctor['speciality'],
            textAlign: TextAlign.center,
            style: TextStyle(
              color: kDarkColor,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        Text(
          doctor['gender'],
          textAlign: TextAlign.center,
          style: TextStyle(
            color: kDarkColor,
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        Expanded(
          child: Text(
            doctor['email'],
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
            doctor['hospitalID'],
            textAlign: TextAlign.center,
            style: TextStyle(
              color: kDarkColor,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        )
      ];
    }

    List<String> accessToDoctors =
        patient != null ? patient['accessToDoctors'].split(",") : [];

    if (_selectedIndex == 0) {
      // List<dynamic> doctors = [
      //   {
      //     "name": "Dr. Jane Dough",
      //     "speciality": "Orthopedic Surgeon",
      //     "gender": "F",
      //     "email": "jane@gmail.com",
      //     "hospital": "Berlin Main Hospital"
      //   },
      // ];
      List<Widget> output = [];

      for (var i = 0; i < doctors.length; i++) {
        if (!accessToDoctors.contains(doctors[i]['doctorID'])) {
          continue;
        }

        output.add(
          BaseCard(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32.0),
              child: Row(children: [
                ...getRowDetails(doctors[i]),
                Container(
                  child: IconButton(
                    icon: Icon(Icons.cancel_outlined),
                    color: Colors.red,
                    tooltip: "Remove doctor from permission list",
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
    } else {
      // List<dynamic> doctors = [
      //   {
      //     "name": "Dr. John Doe",
      //     "speciality": "General Physician",
      //     "gender": "M",
      //     "email": "johndoe@gmail.com",
      //     "hospital": "Munich Hospital"
      //   },
      //   {
      //     "name": "Dr. Alice Bob",
      //     "speciality": "Neuorosyrgeon",
      //     "gender": "F",
      //     "email": "alice@gmail.com",
      //     "hospital": "Schwabing Central Hospital"
      //   },
      // ];
      List<Widget> output = [];

      for (var i = 0; i < doctors.length; i++) {
        if (accessToDoctors.contains(doctors[i]['doctorID'])) {
          continue;
        }
        output.add(
          BaseCard(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32.0),
              child: Row(children: [
                ...getRowDetails(doctors[i]),
                Container(
                  child: IconButton(
                    icon: Icon(Icons.add_circle_outline),
                    color: Colors.green,
                    tooltip: "Add doctor from permission list",
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
              title: 'my\ndoctors',
              icon: Symbols.stethoscope,
            ),
            SizedBox(height: 64),
            getTabs(),
            SizedBox(height: 64),
            ...getDoctors(),
            Spacer(),
          ],
        ),
      ),
    );
  }
}
