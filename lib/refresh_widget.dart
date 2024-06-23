import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class RefreshWidget extends StatefulWidget {
  const RefreshWidget({super.key});

  @override
  State<RefreshWidget> createState() => _RefreshWidgetState();
}

class _RefreshWidgetState extends State<RefreshWidget> {
  static const eventChannel =
      EventChannel('com.example.refresh_tracker/refresh_rate');
  double _refreshRate = 0.0;
  double _fps = 0.0;

  @override
  void initState() {
    _listenToRefreshRate();
    super.initState();
  }

  void _listenToRefreshRate() {
    eventChannel.receiveBroadcastStream().listen((event) {
      final Map<dynamic, dynamic> data = event;
      print(event['fps']);
      setState(() {
        _refreshRate = data['refreshRate'];
        _fps = data['fps'];
      });
    }, onError: (dynamic error) {
      debugPrint("Failed to get refresh rate: $error");
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Text(
          _refreshRate.toStringAsFixed(2),
          style: const TextStyle(
              fontSize: 24, fontWeight: FontWeight.w700, color: Colors.red),
        ),
        const SizedBox(height: 16),
        Text(
          _fps.toStringAsFixed(2),
          style: const TextStyle(
              fontSize: 24, fontWeight: FontWeight.w700, color: Colors.blue),
        ),
      ],
    );
  }
}
