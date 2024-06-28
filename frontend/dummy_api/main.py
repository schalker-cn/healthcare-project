# create basic FastAPI app
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from random import sample

app = FastAPI()

# define origins
origins = [
    "http:localhost:60457",
    "*"
]

# define CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/")
def read_root():
    return {"Hello": "World"}


@app.get("/dummy")
def read_dummy():
    return {"dummy": "data"}


@app.post("/createHealthRecord")
def createHealthRecord():
    return {"status": "success"}


@app.get("/getHealthRecord/{patientID}")
def getHealthRecordByPatientId(patientID: str):
    return {"status": "success", "patientID": patientID}


@app.post("/createMedicine")
def createMedicine():
    return {"status": "success"}


@app.post("/updateMedicine/{patientID}")
def updateMedicine(patientID: str):
    return {"status": "success", "patientID": patientID}


@app.get("/getDoctors")
def getDoctors():
    return [
        {
            "name": "Dr. Alice Bob",
            "speciality": "Cardiologist",
            "hospital": "XYZ Hospital",
            "phone": "+4912354314",
            "email": "alice@gmail.com",
            "speciality": "Cardiologist",
            "doctorId": "246",
            "gender": "F"
        },
        {
            "name": "Dr. John Doe",
            "speciality": "Dermatologist",
            "hospital": "ABC Hospital",
            "phone": "+4912354314",
            "email": "john@gmail.com",
            "doctoreId": "456",
            "gender": "M"
        },
        {
            "name": "Dr. Jane Ed",
            "speciality": "Pediatrician",
            "hospital": "DEF Hospital",
            "phone": "+4912354314",
            "email": "jane@gmail.com",
            "doctorId": "135",
            "gender": "F"
        }
    ]


@app.get("/getPatients")
def getPatients():
    return [
        {
         "name": "Alice Bob",
         "dateOfBirth": "1990-01-01",
         "gender": "F",
         "email": "alicebob@gmail.com",
         "phoneNo": "+4945223368263",
         "patientId": "123"
       },
        {
         "name": "Alex B",
         "dateOfBirth": "1993-05-12",
         "gender": "M",
         "email": "alex@gmail.com",
         "phoneNo": "+4944311368263",
         "patientId": "918"
       },
    ]


@app.post("/updatePatient/{patientID}")
def updatePatient(patientID: str):
    return {"status": "success", "patientID": patientID}


@app.get("/getMedicines")
def getMedicines():
    return [
        {
            "name": "Panadol",
            "productionDate": "2024-01-04",
            "expirationDate": "2026-01-04",
            "amount": "120 Packets",
            "currentOwner": "789",
            "producerId": "101",
        },
        {
            "name": "Vitamin C",
            "productionDate": "2024-01-04",
            "expirationDate": "2024-10-13",
            "amount": "400 Packets",
            "currentOwner": "789",
            "producerId": "101",
        },
        {
            "name": "Fevadol",
            "productionDate": "2023-12-14",
            "expirationDate": "2024-12-14",
            "amount": "100 Packets",
            "currentOwner": "101",
            "producerId": "101",
        },
        {
            "name": "Vitamin D",
            "productionDate": "2024-01-04",
            "expirationDate": "2026-01-04",
            "amount": "220 Packets",
            "currentOwner": "101",
            "producerId": "101",
        },
    ]


@app.get("/getMedicine/{medicineID}")
def getMedicine(medicineID: str):
    return {"status": "success", "medicineID": medicineID}


@app.get("/getHealthRecords")
def getHealthRecords():
    return {"status": "success"}


@app.get("/getHealthRecords/{patientID}")
def getHealthRecordByPatientID(patientID: str):
    records = [
        {
            "dateCreated": "2021-10-10",
            "doctor": "Dr. John Doe",
            "hospital": "Munich Hospital",
            "diagnosis": "Chronic kidney disease",
        },
        {
            "dateCreated": "2019-12-23",
            "doctor": "Dr. Jane Ed",
            "hospital": "Berlin Hospital",
            "diagnosis": "Pneumonia"
        },
        {
            "dateCreated": "2018-12-23",
            "doctor": "Dr. Alice Bob",
            "hospital": "Berlin Hospital",
            "diagnosis": "Asthma"
        },
        {
            "dateCreated": "2017-12-23",
            "doctor": "Dr. Alice Bob",
            "hospital": "Berlin Hospital",
            "diagnosis": "Migraine"
        }
    ]
    
    return sample(records, 2)

#! ####################### Screens: Profile ############################


@app.get("/getPatient/{patientID}")
def getPatient(patientID: str):
    return {"name": "Alice Bob", "dob": "01/01/1990", "phone": "+4912354314", "email": "alice@gmail.com", "doctors": ["135"]}


@app.get("/getDoctor/{doctorID}")
def getDoctor(doctorID: str):
    return {"name": "Dr. Alice Bob", "phone": "+4912354314", "email": "alice@gmail.com", "speciality": "Cardiologist", "hospital": "XYZ Hospital", 'dob': "01/01/1990"}


@app.get("/getHospital/{hospitalID}")
def getHospital(hospitalID: str):
    return {"name": "XYZ Hospital", "phone": "+4912354314", "email": "xyz@gmail.com", "address": "123, ABC Street, Munich, Germany"}


@app.get("/getProducer/{producerID}")
def getProducer(producerID: str):
    return {"name": "ABC Med Factory", "phone": "+4912354314", "email": "xyz@gmail.com", "address": "234, BBB Street, Berlin, Germany"}
