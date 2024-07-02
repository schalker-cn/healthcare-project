import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:frontend/widgets/base_card.dart';
import 'package:frontend/widgets/custom_app_bar.dart';
import 'package:material_symbols_icons/symbols.dart';
import 'package:http/http.dart' as http;
import 'dart:html' as html;

class RecordsScreen extends StatefulWidget {
  const RecordsScreen({
    super.key,
    required this.userType,
    required this.userId,
  });

  static const String screenName = 'records';

  final UserType userType;
  final String userId;

  @override
  State<RecordsScreen> createState() => _RecordsScreenState();
}

class _RecordsScreenState extends State<RecordsScreen> {
  List<dynamic> records = [];

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    fetchRecords();
  }

  void fetchRecords() async {
    var url = getLocalhost(unecodedPath: 'api/getAllHealthRecords');
    var response = await http.get(url);
    dynamic result = jsonDecode(response.body);
    dynamic _records = [];
    print(result);
    if (widget.userType == UserType.patient) {
      for (var i = 0; i < result.length; i++) {
        if (result[i]['patientID'] == widget.userId) {
          _records.add(result[i]);
        }
      }
    } else {
      // url = getLocalhost(unecodedPath: 'api/readPatient/${widget.userId}');
      // response = await http.get(url);
      // dynamic patient = jsonDecode(response.body);
      // print(patient);

      for (var i = 0; i < result.length; i++) {
        if (result[i]['patientID'] == widget.userId) {
          _records.add(result[i]);
        }
      }

      // for (var i = 0; i < result.length; i++) {
      //   if (result[i]['accessToDoctors'] == widget.userId) {
      //     records.add(result[i]);
      //   }
      // }
    }
    setState(() {
      records = _records;
    });
  }

  Future<Map<String, dynamic>> fetchPatientRecord(String recordId) async {
    var url = getLocalhost(unecodedPath: 'api/readHealthRecord/$recordId');
    var response = await http.get(url);
    Map<String, dynamic> result = jsonDecode(response.body);
    print(result);
    return result;
  }

  List<Widget> getRecords() {
    List<Widget> getRowDetails(record) {
      return [
        Expanded(
          child: Text(
            record['date'],
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
            record['doctorID'],
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
            record['symptom'],
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
            record['diagnosis'],
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

    // List<dynamic> records = [
    //   {
    //     "dateCreated": "2021-10-10",
    //     "doctor": "Dr. John Doe",
    //     "hospital": "Munich Hospital",
    //     "diagnosis": "Chronic kidney disease",
    //   }
    // ];

    final size = MediaQuery.of(context).size;
    if (widget.userType == UserType.patient) {
      List<Widget> output = [];

      for (var i = 0; i < records.length; i++) {
        output.add(
          BaseCard(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32.0),
              child: Row(children: [
                ...getRowDetails(records[i]),
                Container(
                  child: IconButton(
                    icon: Icon(Icons.search),
                    color: kPrimaryColor,
                    tooltip: "More details",
                    onPressed: () {
                      showDialog(
                        context: context,
                        builder: (_) {
                          // var result =
                          //     fetchPatientRecord(records[i]['recordID']);
                          // print("resultssss");
                          // print(result);

                          return FutureBuilder(
                            future: fetchPatientRecord(records[i]['recordID']),
                            builder: (_, snapshot) {
                              if (snapshot.connectionState ==
                                  ConnectionState.waiting)
                                return Center(
                                  child: CircularProgressIndicator(),
                                );

                              return Dialog(
                                child: Container(
                                  height: size.height * .5,
                                  padding: const EdgeInsets.all(32),
                                  child: Column(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      Text("Date:  ${snapshot.data!['date']}"),
                                      Text("Symptom: " +
                                          snapshot.data!['symptom']!),
                                      Text("Diagnosis: " +
                                          snapshot.data!['diagnosis']!),
                                      Text("Treatment: " +
                                          snapshot.data!['treatment']!),
                                      Text("Prescription: " +
                                          snapshot.data!['prescriptionID']!),
                                    ],
                                  ),
                                ),
                              );
                            },
                          );
                        },
                      );
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
    } else {
      List<Widget> output = [];

      for (var i = 0; i < records.length; i++) {
        output.add(
          BaseCard(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32.0),
              child: Row(children: [
                ...getRowDetails(records[i]),
                Container(
                  child: IconButton(
                    icon: Icon(Icons.search),
                    color: kPrimaryColor,
                    tooltip: "More details",
                    onPressed: () {
                      showDialog(
                        context: context,
                        builder: (_) {
                          // var result =
                          //     fetchPatientRecord(records[i]['recordID']);
                          // print("resultssss");
                          // print(result);

                          return FutureBuilder(
                            future: fetchPatientRecord(records[i]['recordID']),
                            builder: (_, snapshot) {
                              if (snapshot.connectionState ==
                                  ConnectionState.waiting)
                                return Center(
                                  child: CircularProgressIndicator(),
                                );

                              return Dialog(
                                child: Container(
                                  height: size.height * .5,
                                  padding: const EdgeInsets.all(32),
                                  child: Column(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      Text("Date:  ${snapshot.data!['date']}"),
                                      Text("Symptom: " +
                                          snapshot.data!['symptom']!),
                                      Text("Diagnosis: " +
                                          snapshot.data!['diagnosis']!),
                                      Text("Treatment: " +
                                          snapshot.data!['treatment']!),
                                      Text("Prescription: " +
                                          snapshot.data!['prescriptionID']!),
                                    ],
                                  ),
                                ),
                              );
                            },
                          );
                        },
                      );
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
  }

  Widget textField(String labelText, TextEditingController controller) {
    final size = MediaQuery.of(context).size;
    final border = OutlineInputBorder(
      borderRadius: BorderRadius.circular(0),
      borderSide: BorderSide(color: kLightColor),
    );
    return SizedBox(
      width: size.width * 0.35,
      child: TextField(
        controller: controller,
        cursorColor: kDarkColor,
        style: TextStyle(color: kDarkColor),
        decoration: InputDecoration(
          labelText: labelText,
          labelStyle: TextStyle(color: kDarkColor),
          // hintStyle: TextStyle(color: Colors.grey),
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(0),
            borderSide: BorderSide(color: kPrimaryColor),
          ),
          enabledBorder: border,
          focusedBorder: border,
          errorBorder: border,
          focusedErrorBorder: border,
          // filled: true,
          // fillColor: kli,
          // contentPadding: EdgeInsets.all(20),
        ),
      ),
    );
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
              title: widget.userType == UserType.patient
                  ? 'my\nrecords'
                  : 'patient\nrecords',
              icon: Symbols.note_add,
            ),
            if (widget.userType == UserType.doctor) ...[
              SizedBox(height: 64),
              ElevatedButton(
                onPressed: () {
                  showDialog(
                    context: context,
                    builder: (_) {
                      // generate random integer for record ID
                      var recordId = DateTime.now().millisecondsSinceEpoch;
                      print(recordId);

                      // controllers for date, symptom, diagnosis, treatment, prescription
                      TextEditingController dateController =
                          TextEditingController();
                      TextEditingController symptomController =
                          TextEditingController();
                      TextEditingController diagnosisController =
                          TextEditingController();
                      TextEditingController treatmentController =
                          TextEditingController();
                      TextEditingController prescriptionController =
                          TextEditingController();
                      TextEditingController recordIdController =
                          TextEditingController(text: 'record_$recordId');
                      TextEditingController patientIdController =
                          TextEditingController(text: widget.userId);
                      TextEditingController doctorIdController =
                          TextEditingController();

                      return Dialog(
                        child: Container(
                          // height: size.height * .5,
                          color: kPrimaryColor,
                          padding: const EdgeInsets.all(32),
                          child: SingleChildScrollView(
                            child: Column(
                              children: [
                                textField("Record ID", recordIdController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField("Patient ID", patientIdController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField("Doctor ID", doctorIdController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField("Date", dateController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField("Symptom", symptomController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField("Diagnosis", diagnosisController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField("Treatment", treatmentController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField(
                                    "Prescription", prescriptionController),
                                SizedBox(
                                  height: 16,
                                ),
                                ElevatedButton(
                                  onPressed: () async {
                                    var url = getLocalhost(
                                        unecodedPath:
                                            'api/createHealthRecord/record_$recordId');
                                    var response = await http.post(url,
                                        headers: {
                                          'Content-Type': 'application/json',
                                        },
                                        body: jsonEncode({
                                          "recordID": recordIdController.text,
                                          "patientID": patientIdController.text,
                                          "doctorID": doctorIdController.text,
                                          "date": dateController.text,
                                          "symptom": symptomController.text,
                                          "diagnosis": diagnosisController.text,
                                          "treatment": treatmentController.text,
                                          "prescriptionID":
                                              prescriptionController.text,
                                        }));
                                    print(response);
                                    html.window.location.reload();
                                  },
                                  child: Text(
                                    "Add Record",
                                    style: TextStyle(
                                      fontSize: 24,
                                      color: kDarkColor,
                                    ),
                                  ),
                                  style: ButtonStyle(
                                    backgroundColor:
                                        WidgetStateProperty.all(kLightColor),
                                    padding: WidgetStateProperty.all(
                                        EdgeInsets.symmetric(
                                            vertical: 16, horizontal: 32)),
                                    shape: WidgetStateProperty.all(
                                        RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(0),
                                    )),
                                  ),
                                )
                              ],
                            ),
                          ),
                        ),
                      );
                    },
                  );
                },
                child: Text(
                  'Add Record',
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
            ],
            SizedBox(height: 64),
            ...getRecords(),
          ],
        ),
      ),
    );
  }
}
