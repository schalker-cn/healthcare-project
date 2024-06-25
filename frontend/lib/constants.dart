import 'package:flutter/material.dart';
import 'package:http/http.dart';

Uri getLocalhost({String authority = '127.0.0.1:8000', String? unecodedPath}) =>
    Uri.http(authority, unecodedPath ?? '');

const kPrimaryColor = Color(0xFFF87C5A);
const kDarkColor = Color(0xFF0D0F35);
const kLightColor = Color(0xFFFFF3E7);

enum Gender { male, female }

enum UserType { patient, doctor, hospital, producer }
