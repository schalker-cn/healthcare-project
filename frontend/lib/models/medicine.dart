class Medicine {
  String medicineId;
  String name;
  DateTime productionDate;
  DateTime expirationDate;
  String producerId;
  String currentOwner;
  List<String> previousOwners;

  Medicine({
    required this.medicineId,
    required this.name,
    required this.productionDate,
    required this.expirationDate,
    required this.producerId,
    required this.currentOwner,
    required this.previousOwners,
  });
}
