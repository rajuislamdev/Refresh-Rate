import 'package:flutter/material.dart';
import 'package:refresh_tracker/refresh_widget.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        alignment: Alignment.center,
        children: [
          ListView.builder(
            itemCount: 120,
            itemBuilder: (context, index) => ListTile(
              title: Text('This is title $index'),
              subtitle: Text('This is subtitle $index'),
            ),
          ),
          const Positioned(
            child: RefreshWidget(),
          )
        ],
      ),
    );
  }
}
