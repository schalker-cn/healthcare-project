import 'package:frontend/constants.dart';
import 'package:go_router/go_router.dart';
import 'screens/all_screens.dart';

final router2 = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(
      name: 'landing',
      path: '/',
      builder: (context, state) => const LandingScreen(),
      routes: [
        GoRoute(
          name: 'dashboard',
          path: 'dashboard',
          builder: (context, state) {
            final String userTypeQP =
                state.uri.queryParameters['userType'] ?? UserType.patient.name;
            final UserType userType =
                UserType.values.firstWhere((e) => e.name == userTypeQP);
            final String userId = state.uri.queryParameters['userId'] ?? '123';
            return HomeScreen(userType: userType, userId: userId);
          },
          routes: [
            GoRoute(
              name: 'records',
              path: 'records',
              builder: (context, state) {
                final String userTypeQP =
                    state.uri.queryParameters['userType']!;
                final UserType userType =
                    UserType.values.firstWhere((e) => e.name == userTypeQP);
                final String userId =
                    state.uri.queryParameters['userId'] ?? '123';
                return RecordsScreen(userType: userType, userId: userId);
              },
            ),
            GoRoute(
              name: 'medicines',
              path: 'medicines',
              builder: (context, state) {
                final String userTypeQP =
                    state.uri.queryParameters['userType']!;
                final UserType userType =
                    UserType.values.firstWhere((e) => e.name == userTypeQP);
                final String userId =
                    state.uri.queryParameters['userId'] ?? '123';
                return MedicinesScreen(userType: userType, userId: userId);
              },
            ),
            GoRoute(
              name: 'doctors',
              path: 'doctors',
              builder: (context, state) {
                final String userId =
                    state.uri.queryParameters['userId'] ?? '123';
                return DoctorsScreen(
                  userId: userId,
                );
              },
            ),
            GoRoute(
                name: 'patients',
                path: 'patients',
                builder: (context, state) {
                  final String userId =
                      state.uri.queryParameters['userId'] ?? '123';
                  return PatientsScreen(
                    userId: userId,
                  );
                },
                routes: [
                  GoRoute(
                    name: 'patient-records',
                    path: 'patient-records',
                    builder: (context, state) {
                      final String userTypeQP =
                          state.uri.queryParameters['userType']!;
                      final UserType userType = UserType.values
                          .firstWhere((e) => e.name == userTypeQP);
                      final String userId =
                          state.uri.queryParameters['userId'] ?? '123';
                      return RecordsScreen(
                        userType: userType,
                        userId: userId,
                      );
                    },
                  ),
                ]),
            GoRoute(
              name: 'profile',
              path: 'profile',
              builder: (context, state) {
                final String userTypeQP =
                    state.uri.queryParameters['userType']!;
                final UserType userType =
                    UserType.values.firstWhere((e) => e.name == userTypeQP);
                final String userId =
                    state.uri.queryParameters['userId'] ?? '123';
                return ProfileScreen(
                  userType: userType,
                  userId: userId,
                );
              },
            ),
            GoRoute(
              name: 'users',
              path: 'users',
              builder: (context, state) {
                return UsersScreen();
              },
            ),
          ],
        ),
      ],
    ),
  ],
);

// GoRouter configuration
// final router = GoRouter(
//   routes: [
//     GoRoute(
//       path: '/',
//       builder: (context, state) => const LandingScreen(),
//       routes: [
//         GoRoute(
//           // name: HomeScreen.screenName,
//           path: "/",
//           builder: (context, state) => const HomeScreen(),
//         ),
//       ],
//     ),
//   ],
// );
