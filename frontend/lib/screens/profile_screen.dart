import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:frontend/widgets/custom_app_bar.dart';
import 'package:material_symbols_icons/symbols.dart';
import 'package:http/http.dart' as http;

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({
    super.key,
    required this.userType,
    required this.userId,
  });

  static const String screenName = 'profile';

  final UserType userType;
  final String userId;

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  final nameController = TextEditingController();
  final dobController = TextEditingController();
  final phoneController = TextEditingController();
  final emailController = TextEditingController();
  final addressController = TextEditingController();
  final specialityController = TextEditingController();
  dynamic user;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    fetchUser();
  }

  void fetchUser() async {
    // fetch user data from the server
    if (widget.userType == UserType.patient) {
      var url = getLocalhost(unecodedPath: 'api/readPatient/${widget.userId}');
      var response = await http.get(url);
      dynamic result = jsonDecode(response.body);
      setState(() {
        user = result;
      });
      nameController.text = result['name'];
      dobController.text = result['age'].toString();
      phoneController.text = result['phone'];
      emailController.text = result['email'];
    } else if (widget.userType == UserType.hospital) {
      var url = getLocalhost(unecodedPath: 'api/readHospital/${widget.userId}');
      var response = await http.get(url);
      dynamic result = jsonDecode(response.body);
      setState(() {
        user = result;
      });
      nameController.text = result['name'];
      addressController.text = result['address'];
      phoneController.text = result['phone'];
      emailController.text = result['email'];
    } else if (widget.userType == UserType.doctor) {
      var url = getLocalhost(unecodedPath: 'api/readDoctor/${widget.userId}');
      var response = await http.get(url);
      dynamic result = jsonDecode(response.body);
      print(response.body);
      setState(() {
        user = result;
      });
      nameController.text = result['name'];
      dobController.text = result['age'].toString();
      specialityController.text = result['speciality'];
      phoneController.text = result['phone'];
      emailController.text = result['email'];
    } else if (widget.userType == UserType.producer) {
      var url = getLocalhost(unecodedPath: 'api/readProducer/${widget.userId}');
      var response = await http.get(url);
      dynamic result = jsonDecode(response.body);
      setState(() {
        user = result;
      });
      nameController.text = result['name'];
      addressController.text = result['address'];
      // phoneController.text = result['phone'];
      // emailController.text = result['email'];
    }
  }

  void saveButtonHandler() async {
    if (widget.userType == UserType.patient) {
      var url =
          getLocalhost(unecodedPath: 'api/updatePatient/${widget.userId}');
      user['name'] = nameController.text;
      user['age'] = int.parse(dobController.text);
      user['phone'] = phoneController.text;
      user['email'] = emailController.text;

      var response = await http.put(url, body: user);
      dynamic result = jsonDecode(response.body);
      print(result);
    } else if (widget.userType == UserType.doctor) {
      var url = getLocalhost(unecodedPath: 'api/updateDoctor/${widget.userId}');
      user['name'] = nameController.text;
      user['age'] = int.parse(dobController.text);
      user['speciality'] = specialityController.text;
      user['phone'] = phoneController.text;
      user['email'] = emailController.text;
      var response = await http.put(url, body: user);
      dynamic result = jsonDecode(response.body);
      print(result);
    } else if (widget.userType == UserType.hospital) {
      var url =
          getLocalhost(unecodedPath: 'api/updateHospital/${widget.userId}');
      user['name'] = nameController.text;
      user['address'] = addressController.text;
      user['phone'] = phoneController.text;
      user['email'] = emailController.text;
      var response = await http.put(url, body: user);
      dynamic result = jsonDecode(response.body);
      print(result);
    } else if (widget.userType == UserType.producer) {
      var url =
          getLocalhost(unecodedPath: 'api/updateProducer/${widget.userId}');
      user['name'] = nameController.text;
      user['address'] = addressController.text;
      // user['phone'] = phoneController.text;
      // user['email'] = emailController.text;
      var response = await http.put(url, body: user);
      dynamic result = jsonDecode(response.body);
      print(result);
    }
  }

  Widget saveButton() {
    final size = MediaQuery.of(context).size;
    return SizedBox(
      width: size.width * 0.6,
      child: ElevatedButton(
        onPressed: saveButtonHandler,
        child: Text(
          'Save',
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
    );
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

  List<Widget> getPatientProfile() {
    return [
      Spacer(),
      textField("Name", nameController),
      SizedBox(height: 32),
      textField("Age", dobController),
      SizedBox(height: 32),
      textField("Phone No", phoneController),
      SizedBox(height: 32),
      textField("Email", emailController),
      Spacer(),
      saveButton(),
      Spacer(),
    ];
  }

  List<Widget> getHospitalProfile() {
    return [
      Spacer(),
      textField("Name", nameController),
      SizedBox(height: 32),
      textField("Address", addressController),
      SizedBox(height: 32),
      textField("Phone No", phoneController),
      SizedBox(height: 32),
      textField("Email", emailController),
      Spacer(),
      saveButton(),
      Spacer(),
    ];
  }

  List<Widget> getDoctorProfile() {
    final size = MediaQuery.of(context).size;
    return [
      Spacer(),
      SizedBox(
        width: size.width * 0.6,
        child: Row(
          children: [
            Expanded(
              child: Column(
                children: [
                  textField("Name", nameController),
                  SizedBox(height: 32),
                  textField("Age", dobController),
                  SizedBox(height: 32),
                  textField("Speciality", specialityController),
                ],
              ),
            ),
            SizedBox(width: 32),
            Expanded(
              child: Column(
                children: [
                  textField("Email", emailController),
                  SizedBox(height: 32),
                  textField("Phone No", phoneController),
                  SizedBox(height: 32),
                  textField("Hospital", emailController),
                ],
              ),
            )
          ],
        ),
      ),
      Spacer(),
      saveButton(),
      Spacer(),
    ];
  }

  List<Widget> getProducerProfile() {
    return [
      Spacer(),
      textField("Name", nameController),
      SizedBox(height: 32),
      textField("Address", addressController),
      // SizedBox(height: 32),
      // textField("Phone No", phoneController),
      // SizedBox(height: 32),
      // textField("Email", emailController),
      Spacer(),
      saveButton(),
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
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            CustomAppBar(
              title: 'my\nprofile',
              icon: Symbols.person,
            ),
            ...(widget.userType == UserType.patient
                ? getPatientProfile()
                : widget.userType == UserType.hospital
                    ? getHospitalProfile()
                    : widget.userType == UserType.doctor
                        ? getDoctorProfile()
                        : widget.userType == UserType.producer
                            ? getProducerProfile()
                            : []),
          ],
        ),
      ),
    );
  }
}
