import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';

class BaseCard extends StatelessWidget {
  const BaseCard({
    super.key,
    required this.child,
  });

  final Widget child;

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Container(
      width: size.width * .6,
      height: size.height * .1,
      decoration: BoxDecoration(
        color: kLightColor,
        borderRadius: BorderRadius.circular(16),
      ),
      child: child,
    );
  }
}
