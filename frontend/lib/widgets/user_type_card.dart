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
  List<Map<String, dynamic>> userTypes = [
    {
      'index': 0,
      'name': 'patient',
      'icon': Symbols.coronavirus,
    },
    {
      'index': 1,
      'name': 'doctor',
      'icon': Symbols.stethoscope,
    },
    {
      'index': 2,
      'name': 'hospital',
      'icon': Symbols.local_hospital,
    },
    {
      'index': 3,
      'name': 'producer',
      'icon': Symbols.pill,
    }
  ];

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
      return '123';
    } else if (userTypes[index]['name'] == 'doctor') {
      return '456';
    } else if (userTypes[index]['name'] == 'hospital') {
      return '789';
    } else {
      return '101';
    }
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
        SizedBox(height: 16),
        Container(
          width: size.width * .25,
          child: ElevatedButton(
            onPressed: () {
              GoRouter.of(context).pushNamed('dashboard', queryParameters: {
                'userType': userTypes[index]['name'],
                'userId': getUserId()
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
