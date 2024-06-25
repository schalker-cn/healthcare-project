class Medicine {
  String medicineId;
  String name;
  DateTime productionDate;
  DateTime expirationDate;
  String producerId;
  String currentOwner;
  List<String> previousOwners;
  double amount;

  Medicine({
    required this.medicineId,
    required this.name,
    required this.productionDate,
    required this.expirationDate,
    required this.producerId,
    required this.currentOwner,
    required this.previousOwners,
    required this.amount,
  });
}
