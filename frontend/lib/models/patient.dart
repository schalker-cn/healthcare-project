class Patient {
  String patientId;
  String hospitalId;
  String name;
  DateTime dateOfBirth;
  String gender; //!
  String email;
  String phone;
  List<String> doctors = [];

  Patient({
    required this.patientId,
    required this.hospitalId,
    required this.name,
    required this.dateOfBirth,
    required this.gender,
    required this.email,
    required this.phone,
  });
}
