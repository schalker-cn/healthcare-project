import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:go_router/go_router.dart';

class CustomAppBar extends StatelessWidget {
  const CustomAppBar({
    super.key,
    required this.title,
    required this.icon,
  });

  final String title;
  final IconData icon;

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Container(
      width: size.width,
      height: size.height * 0.2,
      decoration: BoxDecoration(
        color: kPrimaryColor,
      ),
      child: Padding(
        padding: EdgeInsets.symmetric(
          horizontal: size.width * 0.2,
          vertical: size.height * 0.05,
        ),
        child: Stack(
          children: [
            Positioned.fill(
              child: Align(
                alignment: Alignment.topLeft,
                child: MouseRegion(
                  cursor: SystemMouseCursors.click,
                  child: GestureDetector(
                    onTap: () => context.pop(),
                    child: Icon(
                      Icons.arrow_back,
                      color: kLightColor,
                    ),
                  ),
                ),
              ),
              left: 0,
              top: 0,
            ),
            Align(
              alignment: Alignment.center,
              child: Container(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(
                      icon,
                      size: 56,
                      color: kLightColor,
                    ),
                    SizedBox(width: 16),
                    Text(
                      title,
                      style: TextStyle(
                        fontSize: 28,
                        fontWeight: FontWeight.bold,
                        color: kLightColor,
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
