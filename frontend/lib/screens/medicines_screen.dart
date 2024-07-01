import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:frontend/widgets/base_card.dart';
import 'package:frontend/widgets/custom_app_bar.dart';
import 'package:material_symbols_icons/symbols.dart';
import 'package:http/http.dart' as http;

class MedicinesScreen extends StatefulWidget {
  const MedicinesScreen({
    super.key,
    required this.userType,
    required this.userId,
  });

  static const String screenName = 'medicines';

  final UserType userType;
  final String userId;

  @override
  State<MedicinesScreen> createState() => _MedicinesScreenState();
}

class _MedicinesScreenState extends State<MedicinesScreen> {
  List<dynamic> medicines = [];

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    fetchMedicines();
  }

  void fetchMedicines() async {
    // fetch medicines data from the server
    // if (widget.userType == UserType.producer) {
    var url = getLocalhost(unecodedPath: 'api/getAllMedicines');
    var response = await http.get(url);
    dynamic result = jsonDecode(response.body);
    setState(() {
      medicines = result;
    });
    print(medicines);
    // } else {
    // var url = getLocalhost(unecodedPath: '/getHospitalMedicines');
    // var response = await http.get(url);
    // dynamic result = jsonDecode(response.body);
    // setState(() {
    //   medicines = result;
    // });
    // }
  }

  Future<dynamic> fetchDoctors() async {
    var url = getLocalhost(unecodedPath: 'api/getAllHospitals');
    var response = await http.get(url);
    dynamic result = jsonDecode(response.body);
    print(response.body);
    return result;
  }

  String _value = 'hospital_1';

  List<Widget> getRecords() {
    List<Widget> getRowDetails(medicine) {
      return [
        Expanded(
          child: Text(
            medicine['name'],
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
            medicine['productionDate'],
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
            medicine['expirationDate'],
            textAlign: TextAlign.center,
            style: TextStyle(
              color: kDarkColor,
              fontSize: 16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        // Expanded(
        //   child: Text(
        //     medicine['amount'],
        //     textAlign: TextAlign.center,
        //     style: TextStyle(
        //       color: kDarkColor,
        //       fontSize: 16,
        //       fontWeight: FontWeight.bold,
        //     ),
        //   ),
        // ),
        Expanded(
          child: Text(
            "Owned by " +
                (medicine['currentOwner'] == widget.userId
                    ? 'You'
                    : '' + medicine['currentOwner']),
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

    if (widget.userType == UserType.producer) {
      List<Widget> output = [];

      for (var i = 0; i < medicines.length; i++) {
        if (medicines[i]['producerID'] != widget.userId) {
          continue;
        }
        // print(medicines[i]['currentOwner']);
        output.add(
          BaseCard(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32.0),
              child: Row(children: [
                ...getRowDetails(medicines[i]),
                Container(
                  child: IconButton(
                    icon: Icon(Icons.move_down),
                    color: kPrimaryColor,
                    tooltip: medicines[i]['currentOwner'] == widget.userId
                        ? "Transfer ownership to hospital"
                        : "This medicine has already been transfered to a hospital.",
                    onPressed: medicines[i]['currentOwner'] == widget.userId
                        ? () {
                            final size = MediaQuery.of(context).size;
                            medicines[i]['previousOwners'] =
                                medicines[i]['currentOwner'];
                            showDialog(
                                context: context,
                                builder: (_) {
                                  return FutureBuilder(
                                    future: fetchDoctors(),
                                    builder: (_, snapshot) {
                                      if (snapshot.connectionState ==
                                          ConnectionState.waiting) {
                                        return Center(
                                          child: CircularProgressIndicator(),
                                        );
                                      }

                                      // init dropdown

                                      List<DropdownMenuItem<String>>
                                          dropdownItems = [];

                                      for (var hospital in snapshot.data) {
                                        dropdownItems.add(DropdownMenuItem(
                                          value: hospital["hospitalID"],
                                          child: Text(hospital["name"]),
                                        ));
                                      }

                                      return StatefulBuilder(
                                        builder: (_, setStateSB) => Dialog(
                                          child: Container(
                                            height: size.height * .5,
                                            color: kPrimaryColor,
                                            padding: const EdgeInsets.all(32),
                                            child: Column(
                                              mainAxisAlignment:
                                                  MainAxisAlignment.center,
                                              children: [
                                                Text("Select Hospital"),
                                                DropdownButton(
                                                  value: _value,
                                                  items: dropdownItems,
                                                  onChanged: (val) {
                                                    setStateSB(() {
                                                      _value = val!;
                                                    });
                                                  },
                                                ),
                                                SizedBox(
                                                  height: 32,
                                                ),
                                                ElevatedButton(
                                                  onPressed: () async {
                                                    var url = getLocalhost(
                                                        unecodedPath:
                                                            'api/updateMedicine/${medicines[i]['medicineID']}');
                                                    medicines[i]
                                                            ['previousOwners'] =
                                                        medicines[i]
                                                            ['currentOwner'];
                                                    medicines[i]
                                                            ['currentOwner'] =
                                                        _value;

                                                    var response =
                                                        await http.put(
                                                      url,
                                                      headers: {
                                                        'Content-Type':
                                                            'application/json'
                                                      },
                                                      body: jsonEncode(
                                                          medicines[i]),
                                                    );
                                                    print(response);
                                                    print(response.statusCode);
                                                  },
                                                  child: Text(
                                                    "Transfer ownership",
                                                    style: TextStyle(
                                                      fontSize: 24,
                                                      color: kDarkColor,
                                                    ),
                                                  ),
                                                  style: ButtonStyle(
                                                    backgroundColor:
                                                        WidgetStateProperty.all(
                                                            kLightColor),
                                                    padding:
                                                        WidgetStateProperty.all(
                                                            EdgeInsets
                                                                .symmetric(
                                                                    vertical:
                                                                        16,
                                                                    horizontal:
                                                                        32)),
                                                    shape:
                                                        WidgetStateProperty.all(
                                                      RoundedRectangleBorder(
                                                        borderRadius:
                                                            BorderRadius
                                                                .circular(0),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ],
                                            ),
                                          ),
                                        ),
                                      );
                                    },
                                  );
                                });
                          }
                        : null,
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
      // List<dynamic> medicines = [
      //   {
      //     "name": "Vitamin C",
      //     "productionDate": "2024-05-15",
      //     "expirationDate": "2026-10-25",
      //     "amount": "320 Packets",
      //     "owner": "You"
      //   }
      // ];

      List<Widget> output = [];

      for (var i = 0; i < medicines.length; i++) {
        if (medicines[i]['currentOwner'] != widget.userId) {
          continue;
        }
        output.add(
          BaseCard(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32.0),
              child: Row(children: [
                ...getRowDetails(medicines[i]),
                // Container(
                //   child: IconButton(
                //     icon: Icon(Icons.move_down),
                //     color: kPrimaryColor,
                //     tooltip: medicines[i]['owner'] == 'You'
                //         ? "Transfer ownership to hospital"
                //         : "This medicine has already been transfered to a hospital.",
                //     onPressed: medicines[i]['owner'] == 'You' ? () {} : null,
                //   ),
                // )
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
        cursorColor: kLightColor,
        style: TextStyle(color: kLightColor),
        decoration: InputDecoration(
          labelText: labelText,
          labelStyle: TextStyle(color: kLightColor),
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
          // fillColor: Colors.white,
          // contentPadding: EdgeInsets.all(20),
        ),
      ),
    );
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
              title: widget.userType == UserType.producer
                  ? 'all\nmedicines'
                  : 'my\nmedicines',
              icon: Symbols.pill,
            ),
            if (widget.userType == UserType.producer) ...[
              SizedBox(height: 64),
              ElevatedButton(
                onPressed: () {
                  showDialog(
                    context: context,
                    builder: (_) {
                      // generate random integer for record ID
                      var recordId = DateTime.now().millisecondsSinceEpoch;
                      // print(recordId);

                      //           "medicineID": "medicine_1",
                      // "producerID": "producer_1",
                      // "name": "Aspirin",
                      // "productionDate": "2023-01-15",
                      // "expirationDate": "2025-01-15",
                      // "currentOwner": "producer_1",
                      // "previousOwners": "producer_1"

                      // controllers for date, symptom, diagnosis, treatment, prescription

                      TextEditingController nameController =
                          TextEditingController();
                      TextEditingController productionDateController =
                          TextEditingController();
                      TextEditingController expirationDateController =
                          TextEditingController();
                      TextEditingController currentOwnerController =
                          TextEditingController(text: widget.userId);
                      TextEditingController medicineIdController =
                          TextEditingController(text: 'medicine_$recordId');
                      TextEditingController producerIdController =
                          TextEditingController(text: widget.userId);
                      // TextEditingController Controller =
                      // TextEditingController();

                      return Dialog(
                        child: Container(
                          // height: size.height * .5,
                          color: kPrimaryColor,
                          padding: const EdgeInsets.all(32),
                          child: SingleChildScrollView(
                            child: Column(
                              children: [
                                textField("Medicine ID", medicineIdController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField("Producer ID", producerIdController),
                                SizedBox(
                                  height: 16,
                                ),
                                // textField("Doctor ID", Controller),
                                // SizedBox(
                                //   height: 16,
                                // ),
                                textField("Name", nameController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField("Production Date",
                                    productionDateController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField("Expiration Date",
                                    expirationDateController),
                                SizedBox(
                                  height: 16,
                                ),
                                textField(
                                    "Current Owner", currentOwnerController),
                                SizedBox(
                                  height: 16,
                                ),
                                ElevatedButton(
                                  onPressed: () async {
                                    var url = getLocalhost(
                                        unecodedPath:
                                            'api/createMedicine/medicine_$recordId');

                                    var body = {
                                      "medicineID": medicineIdController.text,
                                      "producerID": producerIdController.text,
                                      // "doctorID": Controller.text,
                                      // "date": dateController.text,
                                      "name": nameController.text,
                                      "productionDate":
                                          productionDateController.text,
                                      "expirationDate":
                                          expirationDateController.text,
                                      "currentOwner":
                                          currentOwnerController.text,
                                      "previousOwners": ""
                                    };

                                    print("body:");
                                    print(body);
                                    print(body.runtimeType);

                                    print("encoded:");
                                    print(jsonEncode(body));
                                    print(jsonEncode(body).runtimeType);

                                    print("tostring");
                                    print(body.toString());
                                    print(body.toString().runtimeType);

                                    var response = await http.post(
                                      url,
                                      headers: {
                                        'Content-Type': 'application/json'
                                      },
                                      // body: body.toString(),
                                      body: jsonEncode(body),
                                      // encoding:
                                    );
                                    print(response.body);
                                    print(response.statusCode);
                                  },
                                  child: Text(
                                    "Add Medicine",
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
                  "Add Medicine",
                  style: TextStyle(
                    fontSize: 24,
                    color: Colors.white,
                  ),
                ),
                style: ButtonStyle(
                  backgroundColor: WidgetStateProperty.all(kPrimaryColor),
                  padding: WidgetStateProperty.all(
                      EdgeInsets.symmetric(vertical: 16, horizontal: 32)),
                  shape: WidgetStateProperty.all(
                    RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(0),
                    ),
                  ),
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
