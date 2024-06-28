import 'package:frontend/models/prescription.dart';

class HealthRecord {
  String healthRecordId;
  DateTime dateCreated;
  String symptoms;
  String diagnosis;
  String treatment;
  List<Prescription> prescriptions;
  String patientId;
  String doctorId;

  HealthRecord({
    required this.healthRecordId,
    required this.dateCreated,
    required this.symptoms,
    required this.diagnosis,
    required this.treatment,
    required this.prescriptions,
    required this.patientId,
    required this.doctorId,
  });
}
