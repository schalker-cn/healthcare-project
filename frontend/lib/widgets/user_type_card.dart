import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:go_router/go_router.dart';
import 'package:material_symbols_icons/symbols.dart';

class UserTypeCards extends StatefulWidget {
  const UserTypeCards({super.key});

  @override
  State<UserTypeCards> createState() => _UserTypeCardsState();
}

class _UserTypeCardsState extends State<UserTypeCards> {
  int index = 0;
  String textFieldLabel = 'Patient ID';
  String textFieldValue = 'patient_1';
  TextEditingController textEditingController =
      TextEditingController(text: 'patient_1');
  List<Map<String, dynamic>> userTypes = [
    {
      'index': 0,
      'name': 'patient',
      'icon': Symbols.coronavirus,
      'textFieldLabel': 'Patient ID',
      'textFieldValue': 'patient_1'
    },
    {
      'index': 1,
      'name': 'doctor',
      'icon': Symbols.stethoscope,
      'textFieldLabel': 'Doctor ID',
      'textFieldValue': 'doctor_1'
    },
    {
      'index': 2,
      'name': 'hospital',
      'icon': Symbols.local_hospital,
      'textFieldLabel': 'Hospital ID',
      'textFieldValue': 'hospital_1'
    },
    {
      'index': 3,
      'name': 'producer',
      'icon': Symbols.pill,
      'textFieldLabel': 'Producer ID',
      'textFieldValue': 'producer_1'
    }
  ];

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    // textEditingController.text = textFieldValue;
  }

  List<Widget> getCards() {
    List<Widget> cards = [];

    final size = MediaQuery.of(context).size;
    for (var i = 0; i < userTypes.length; i++) {
      cards.add(
        MouseRegion(
          cursor: SystemMouseCursors.click,
          child: GestureDetector(
            onTap: () {
              setState(() {
                index = i;
                textFieldLabel = userTypes[i]['textFieldLabel'];
                textFieldValue = userTypes[i]['textFieldValue'];
                textEditingController.text = textFieldValue;
              });
            },
            child: Container(
              width: size.width * .25,
              height: 100,
              color: index == i ? kPrimaryColor : kPrimaryColor.withOpacity(.2),
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 32.0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    Icon(
                      userTypes[i]['icon'],
                      size: 48,
                      color: Colors.white,
                    ),
                    SizedBox(
                      width: 32,
                    ),
                    Text(
                      userTypes[i]['name'],
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 40,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      );
      cards.add(SizedBox(height: 16));
    }
    return cards;
  }

  String getUserId() {
    if (userTypes[index]['name'] == 'patient') {
      return 'patient_1';
    } else if (userTypes[index]['name'] == 'doctor') {
      return 'doctor_1';
    } else if (userTypes[index]['name'] == 'hospital') {
      return 'hospital_1';
    } else {
      return 'producer_1';
    }
  }

  Widget textField(String labelText, TextEditingController controller) {
    final size = MediaQuery.of(context).size;
    final border = OutlineInputBorder(
      borderRadius: BorderRadius.circular(0),
      borderSide: BorderSide(color: kLightColor),
    );
    return SizedBox(
      width: size.width * 0.6,
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
    final size = MediaQuery.of(context).size;
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Text(
          "I'm a...",
          style: TextStyle(
            color: Colors.white,
            fontSize: 32,
          ),
        ),
        SizedBox(height: 32),
        ...getCards(),
        SizedBox(height: 32),
        Container(
            width: size.width * .25,
            child: textField(textFieldLabel, textEditingController)
            // child: TextField(
            //   controller: textEditingController,
            //   decoration: InputDecoration(labelText: textFieldLabel),
            // ),
            ),
        // TextField(
        //   controller: textEditingController,
        //   decoration: InputDecoration(labelText: textFieldLabel),
        // ),
        SizedBox(height: 32),
        Container(
          width: size.width * .25,
          child: ElevatedButton(
            onPressed: () {
              GoRouter.of(context).pushNamed('dashboard', queryParameters: {
                'userType': userTypes[index]['name'],
                // 'userId': getUserId()
                'userId': textEditingController.text
              });
              // context.go(Uri(
              //   path: "/dashboard",
              //   queryParameters: {'userType': userTypes[index]['name']},
              // ).toString());
            },
            child: Text("Continue",
                style: TextStyle(
                  fontSize: 24,
                  color: Colors.white,
                )),
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
    );
  }
}
