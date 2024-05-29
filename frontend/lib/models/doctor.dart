import 'package:frontend/constants.dart';

class Doctor {
  String doctorId;
  String name;
  String dateOfBirth;
  Gender gender;
  String email;
  String phone;
  String address;
  String speciality;
  List<String> patients = [];
  String hospitalId;

  Doctor({
    required this.doctorId,
    required this.name,
    required this.dateOfBirth,
    required this.gender,
    required this.email,
    required this.phone,
    required this.address,
    required this.speciality,
    required this.hospitalId,
  });
}
