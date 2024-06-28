import 'package:flutter/material.dart';
import 'package:frontend/constants.dart';
import 'package:go_router/go_router.dart';

class NavCard extends StatelessWidget {
  NavCard({
    super.key,
    this.queryParameters = const {},
    required this.path,
    required this.icon,
    required this.name,
  });

  final String path;
  final IconData icon;
  final String name;
  final Map<String, dynamic> queryParameters;

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;

    return MouseRegion(
      cursor: SystemMouseCursors.click,
      child: GestureDetector(
        onTap: () {
          // context.go("/$path");
          // context.go(
          // Uri(path: "/$path", queryParameters: queryParameters).toString());
          // context.goNamed("$path", queryParameters: queryParameters);
          GoRouter.of(context)
              .pushNamed(path, queryParameters: queryParameters);
        },
        child: Container(
          width: size.width * .25,
          height: size.width * .25,
          color: kPrimaryColor,
          child: Padding(
            padding: const EdgeInsets.all(64),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Spacer(),
                Icon(
                  icon,
                  size: 56,
                  color: kLightColor,
                ),
                Spacer(),
                Text(
                  name,
                  style: TextStyle(
                    color: kLightColor,
                    fontSize: 48,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                Spacer(),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
