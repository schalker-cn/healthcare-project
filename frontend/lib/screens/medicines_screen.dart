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
    var url = getLocalhost(unecodedPath: 'getMedicines');
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
        Expanded(
          child: Text(
            medicine['amount'],
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
            "Owned by " +
                (medicine['currentOwner'] == widget.userId
                    ? 'You'
                    : 'Hospital ' + medicine['currentOwner']),
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
        if (medicines[i]['producerId'] != widget.userId) {
          continue;
        }

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
                    tooltip: medicines[i]['owner'] == 'You'
                        ? "Transfer ownership to hospital"
                        : "This medicine has already been transfered to a hospital.",
                    onPressed: medicines[i]['owner'] == 'You' ? () {} : null,
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
              title: 'all\nmedicines',
              icon: Symbols.pill,
            ),
            SizedBox(height: 64),
            ...getRecords(),
          ],
        ),
      ),
    );
  }
}
