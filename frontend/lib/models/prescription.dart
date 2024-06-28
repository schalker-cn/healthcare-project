class Prescription {
  String prescriptionId;
  String medicineId;
  int dosage; //! amount
  int duration;

  Prescription({
    required this.prescriptionId,
    required this.medicineId,
    required this.dosage,
    required this.duration,
  });
}
