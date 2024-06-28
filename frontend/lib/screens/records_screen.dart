import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:frontend/widgets/base_card.dart';
import 'package:frontend/widgets/custom_app_bar.dart';
import 'package:material_symbols_icons/symbols.dart';
import 'package:http/http.dart' as http;

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
    var url = getLocalhost(unecodedPath: 'getHealthRecords/${widget.userId}');
    var response = await http.get(url);
    dynamic result = jsonDecode(response.body);
    setState(() {
      records = result;
    });
  }

  List<Widget> getRecords() {
    List<Widget> getRowDetails(record) {
      return [
        Expanded(
          child: Text(
            record['dateCreated'],
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
            record['doctor'],
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
            record['hospital'],
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
              title: widget.userType == UserType.patient
                  ? 'my\nrecords'
                  : 'patient\nrecords',
              icon: Symbols.note_add,
            ),
            SizedBox(height: 64),
            ...getRecords(),
          ],
        ),
      ),
    );
  }
}
